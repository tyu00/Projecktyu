package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitMQConfiguration {

    @Bean
    public ConnectionFactory connectionFactory(
            String rabbitHost,
            int rabbitPort,
            String rabbitVirtualHost,
            String rabbitUser,
            String rabbitPassword
    ) {
        var connectionFactory = new CachingConnectionFactory(rabbitHost, rabbitPort);
        connectionFactory.setUsername(rabbitUser);
        connectionFactory.setPassword(rabbitPassword);
        connectionFactory.setVirtualHost(rabbitVirtualHost);
        return connectionFactory;
    }

    @Bean
    public Queue queue(String rabbitQueue, String rabbitRoutingKey, String rabbitExchange) {
        return QueueBuilder.durable(rabbitQueue)
                .withArgument("x-dead-letter-exchange", rabbitExchange)
                .withArgument("x-dead-letter-routing-key", rabbitRoutingKey + ".dlq")
                .build();
    }

    @Bean
    public DirectExchange directExchange(String rabbitExchange) {
        return new DirectExchange(rabbitExchange, true, false);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange, String rabbitRoutingKey) {
        return BindingBuilder.bind(queue).to(directExchange).with(rabbitRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter("*");
    }

}
