package ru.tinkoff.edu.java.scrapper.service.api;

import ru.tinkoff.edu.java.common.model.LinkUpdateType;

import java.net.URI;
import java.util.List;

public interface BotNotifier {

    void notify(long id, URI url, LinkUpdateType updateType, List<Long> tgChatIds);
}
