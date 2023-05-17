package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Properties {

    @Bean
    public String botToken(ApplicationConfig config) {
        return config.bot().token();
    }

    @Bean
    public String botUsername(ApplicationConfig config) {
        return config.bot().username();
    }

    @Bean
    public String rabbitQueue(ApplicationConfig config) {
        return config.rabbit().queue();
    }

    @Bean
    public String rabbitExchange(ApplicationConfig config) {
        return config.rabbit().exchange();
    }

    @Bean
    public String rabbitUser(ApplicationConfig config) {
        return config.rabbit().user();
    }

    @Bean
    public String rabbitPassword(ApplicationConfig config) {
        return config.rabbit().password();
    }

    @Bean
    public String rabbitHost(ApplicationConfig config) {
        return config.rabbit().host();
    }

    @Bean
    public int rabbitPort(ApplicationConfig config) {
        return config.rabbit().port();
    }

    @Bean
    public String rabbitVirtualHost(ApplicationConfig config) {
        return config.rabbit().virtualHost();
    }

    @Bean
    public String rabbitRoutingKey(ApplicationConfig config) {
        return config.rabbit().routingKey();
    }
}
