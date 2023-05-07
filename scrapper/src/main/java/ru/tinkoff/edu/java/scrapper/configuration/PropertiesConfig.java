package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Bean
    public long schedulerIntervalMs(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }

    @Bean
    public long expirationIntervalMs(ApplicationConfig config) {
        return config.expiration().interval().toMillis();
    }

    @Bean
    public String url(ApplicationConfig config) {
        return config.db().url();
    }

    @Bean
    public String user(ApplicationConfig config) {
        return config.db().user();
    }

    @Bean boolean useQueue(ApplicationConfig config) {
        return config.useQueue();
    }

    @Bean
    public String password(ApplicationConfig config) {
        return config.db().password();
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
