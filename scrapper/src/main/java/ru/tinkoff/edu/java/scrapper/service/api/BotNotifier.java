package ru.tinkoff.edu.java.scrapper.service.api;

import java.net.URI;
import java.util.List;
import ru.tinkoff.edu.java.common.model.LinkUpdateType;

public interface BotNotifier {

    void notify(long id, URI url, LinkUpdateType updateType, List<Long> tgChatIds);
}
