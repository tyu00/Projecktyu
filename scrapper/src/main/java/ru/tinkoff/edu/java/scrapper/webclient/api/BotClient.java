package ru.tinkoff.edu.java.scrapper.webclient.api;

import java.net.URI;
import java.util.List;
import ru.tinkoff.edu.java.common.model.LinkUpdateType;

public interface BotClient {

    void sendUpdate(long id, URI url, LinkUpdateType updateType, List<Long> tgChatIds);
}
