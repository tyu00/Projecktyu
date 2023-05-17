package ru.tinkoff.edu.java.bot.configuration;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.tinkoff.edu.java.bot.telegram.TelegramBot;
import ru.tinkoff.edu.java.bot.telegram.handler.LinkUpdateHandler;
import ru.tinkoff.edu.java.bot.telegram.handler.TelegramUserMessageUpdateHandler;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    public BotCommand startCommand() {
        return new BotCommand("/start", "зарегистрироваться для отслеживания ссылок");
    }

    @Bean
    public BotCommand helpCommand() {
        return new BotCommand("/help", "справка по командам");
    }

    @Bean
    public BotCommand trackCommand() {
        return new BotCommand("/track", "начать отслеживание ссылки");
    }

    @Bean
    public BotCommand untrackCommand() {
        return new BotCommand("/untrack", "прекратить отслеживание ссылки");
    }

    @Bean
    public BotCommand listCommand() {
        return new BotCommand("/list", "показать список отслеживаемых ссылок");
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:bot_messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public TelegramBot telegramBot(
            String botUsername,
            String botToken,
            List<BotCommand> botCommands,
            TelegramBotsApi api,
            TelegramUserMessageUpdateHandler handler,
            LinkUpdateHandler linkUpdateHandler,
            @Value("${spring.application.name}") String applicationName
    ) throws TelegramApiException {
        var bot = new TelegramBot(botUsername, botToken, botCommands, handler, linkUpdateHandler, applicationName);
        api.registerBot(bot);
        bot.initBot();
        return bot;
    }
}
