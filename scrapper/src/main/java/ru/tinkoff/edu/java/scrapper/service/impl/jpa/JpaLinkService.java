package ru.tinkoff.edu.java.scrapper.service.impl.jpa;

import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.jpa.repository.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.repository.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.model.jpa.JpaLink;
import ru.tinkoff.edu.java.scrapper.exception.DatabaseException;
import ru.tinkoff.edu.java.scrapper.service.api.LinkService;
import ru.tinkoff.edu.java.scrapper.service.model.Link;

import java.net.URI;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyMap;

@Transactional
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    public JpaLinkService(JpaLinkRepository linkRepository, JpaChatRepository chatRepository) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public Link add(long tgChatId, URI url) {
        var chat = chatRepository.findById(tgChatId)
                .orElseThrow(() -> new DatabaseException(String.format("No chat is present with tgChatId=[%d]", tgChatId)));
        var link = linkRepository.findByLink(url.toString());
        JpaLink jpaLink;

        if (link.isPresent()) {
            jpaLink = link.get();
            link.get().getTrackingJpaChats().add(chat);
            linkRepository.save(link.get());
        } else {
            jpaLink = JpaLink.builder()
                    .link(url.toString())
                    .trackingJpaChats(Set.of(chat))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .updateInfo(emptyMap())
                    .build();
            linkRepository.save(jpaLink);
        }
        return convert(jpaLink);
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        var foundLink = linkRepository.findByLink(url.toString()).get();
        foundLink.getTrackingJpaChats().removeIf(c -> c.getTgChatId() == tgChatId);
        linkRepository.save(foundLink);
        return convert(foundLink);
    }

    @Override
    public List<Link> getTrackingLinks(long tgChatId) {
        return linkRepository.findByTrackingJpaChatsTgChatId(tgChatId).stream().map(this::convert).toList();
    }

    private Link convert(JpaLink source) {
        return new Link(
                source.getId(),
                URI.create(source.getLink()),
                OffsetDateTime.ofInstant(source.getUpdatedAt().toInstant(), ZoneId.systemDefault()),
                source.getUpdateInfo()
        );
    }
}
