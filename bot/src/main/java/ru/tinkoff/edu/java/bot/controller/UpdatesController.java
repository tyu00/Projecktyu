package ru.tinkoff.edu.java.bot.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.service.BotNotifier;
import ru.tinkoff.edu.java.common.model.LinkUpdate;

@RestController
@RequestMapping("/updates")
public class UpdatesController {

    private final BotNotifier botNotifier;

    public UpdatesController(@Qualifier("httpBotNotifier") BotNotifier botNotifier) {
        this.botNotifier = botNotifier;
    }

    @PostMapping
    public void sendUpdate(@RequestBody LinkUpdate update) {
        botNotifier.notify(update);
    }
}
