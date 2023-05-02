package ru.tinkoff.edu.java.scrapper.service.impl;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.common.model.LinkUpdate;

@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class ScrapperQueueProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String rabbitExchange;
    private final String rabbitRoutingKey;
    private final MessageConverter messageConverter;
    private final MessageProperties messageProperties;

    public ScrapperQueueProducer(RabbitTemplate rabbitTemplate, String rabbitExchange, String rabbitRoutingKey, MessageConverter messageConverter) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitExchange = rabbitExchange;
        this.rabbitRoutingKey = rabbitRoutingKey;
        this.messageConverter = messageConverter;
        messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
    }

    public void send(LinkUpdate update) {
        var messageToSend = messageConverter.toMessage(update, messageProperties);
        rabbitTemplate.convertAndSend(rabbitExchange, rabbitRoutingKey, messageToSend);
    }
}
