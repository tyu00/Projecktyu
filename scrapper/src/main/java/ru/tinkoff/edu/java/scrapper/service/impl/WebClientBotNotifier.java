package ru.tinkoff.edu.java.scrapper.service.impl;

import java.net.URI;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.common.model.LinkUpdateType;
import ru.tinkoff.edu.java.scrapper.service.api.BotNotifier;
import ru.tinkoff.edu.java.scrapper.webclient.api.BotClient;

@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class WebClientBotNotifier implements BotNotifier {

    private final BotClient botClient;

    public WebClientBotNotifier(BotClient botClient) {
        this.botClient = botClient;
    }

    @Override
    public void notify(long id, URI url, LinkUpdateType updateType, List<Long> tgChatIds) {
        botClient.sendUpdate(id, url, updateType, tgChatIds);
    }
}
