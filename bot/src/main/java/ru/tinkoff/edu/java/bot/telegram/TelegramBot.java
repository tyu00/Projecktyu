package ru.tinkoff.edu.java.bot.telegram;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tinkoff.edu.java.bot.telegram.handler.LinkUpdateHandler;
import ru.tinkoff.edu.java.bot.telegram.handler.TelegramUpdateHandler;
import ru.tinkoff.edu.java.common.model.LinkUpdateType;

public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(TelegramBot.class);

    private final String botUsername;
    private final List<BotCommand> botCommands;
    private final TelegramUpdateHandler<SendMessage> updateHandler;
    private final LinkUpdateHandler linkUpdateHandler;
    private final Counter processedMessagesCounter;

    public TelegramBot(
            String botUsername,
            String botToken,
            List<BotCommand> botCommands,
            TelegramUpdateHandler<SendMessage> updateHandler,
            LinkUpdateHandler linkUpdateHandler,
            String applicationName
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.botCommands = botCommands;
        this.updateHandler = updateHandler;
        this.linkUpdateHandler = linkUpdateHandler;
        this.processedMessagesCounter = Metrics
            .counter("processed_messages_count", "application", applicationName);
    }

    public void initBot() throws TelegramApiException {
        execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(updateHandler.handle(update));
            processedMessagesCounter.increment();
        } catch (TelegramApiException e) {
            LOGGER.error("Couldn't process the update [{}].", update);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public void notifyAboutLinkUpdate(String url, LinkUpdateType updateType, List<Long> tgChatsIds) {
        tgChatsIds.forEach(
                id -> {
                    try {
                        execute(linkUpdateHandler.handle(url, updateType, id));
                    } catch (TelegramApiException e) {
                        LOGGER.error(
                                "Couldn't send the update notification about link [{}] to chat [tgChatId={}].",
                                url, id
                        );
                    }
                }
        );
    }

}
