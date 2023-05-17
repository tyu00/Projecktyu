package ru.tinkoff.edu.java.scrapper.service.impl;

import java.net.URI;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.common.model.LinkUpdate;
import ru.tinkoff.edu.java.common.model.LinkUpdateType;
import ru.tinkoff.edu.java.scrapper.service.api.BotNotifier;

@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitMQBotNotifier implements BotNotifier {

    private final ScrapperQueueProducer queueProducer;

    public RabbitMQBotNotifier(ScrapperQueueProducer queueProducer) {
        this.queueProducer = queueProducer;
    }

    @Override
    public void notify(long id, URI url, LinkUpdateType updateType, List<Long> tgChatIds) {
        queueProducer.send(new LinkUpdate(id, url, updateType, tgChatIds));
    }
}
