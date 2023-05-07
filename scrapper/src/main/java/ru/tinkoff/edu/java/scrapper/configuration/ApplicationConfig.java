package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.configuration.dto.AccessType;
import ru.tinkoff.edu.java.scrapper.configuration.dto.Db;
import ru.tinkoff.edu.java.scrapper.configuration.dto.Expiration;
import ru.tinkoff.edu.java.scrapper.configuration.dto.Rabbit;
import ru.tinkoff.edu.java.scrapper.configuration.dto.Scheduler;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String test,
        @NotNull Scheduler scheduler,
        @NotNull Expiration expiration,
        @NotNull Db db,
        @NotNull AccessType accessType,
        @NotNull Rabbit rabbit,
        @NotNull boolean useQueue
) {}
