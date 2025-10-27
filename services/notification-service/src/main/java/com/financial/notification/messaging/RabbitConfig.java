package com.financial.notification.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Exchange
    @Value("${rabbitmq.exchange.investment}")
    private String investmentExchange;

    // Queues
    @Value("${rabbitmq.queue.investment-created}")
    private String investmentCreatedQueue;

    @Value("${rabbitmq.queue.investment-withdrawn}")
    private String investmentWithdrawnQueue;

    @Value("${rabbitmq.queue.investment-notification-dlq}")
    private String investmentNotificationDlq;

    // Routing Keys
    @Value("${rabbitmq.routing-key.investment-created}")
    private String investmentCreatedRoutingKey;

    @Value("${rabbitmq.routing-key.investment-withdrawn}")
    private String investmentWithdrawnRoutingKey;

    @Value("${rabbitmq.routing-key.investment-notification-dlq}")
    private String investmentNotificationDlqRoutingKey;

    // DLQ Configuration
    @Bean
    public TopicExchange investmentExchange() {
        return new TopicExchange(investmentExchange, true, false);
    }

    // Queues principais
    @Bean
    public Queue investmentCreatedQueue() {
        return QueueBuilder.durable(investmentCreatedQueue)
                .withArgument("x-dead-letter-exchange", investmentExchange)
                .withArgument("x-dead-letter-routing-key", investmentNotificationDlqRoutingKey)
                .build();
    }

    @Bean
    public Queue investmentWithdrawnQueue() {
        return QueueBuilder.durable(investmentWithdrawnQueue)
                .withArgument("x-dead-letter-exchange", investmentExchange)
                .withArgument("x-dead-letter-routing-key", investmentNotificationDlqRoutingKey)
                .build();
    }

    // Dead Letter Queue
    @Bean
    public Queue investmentNotificationDlq() {
        return new Queue(investmentNotificationDlq, true);
    }

    // Bindings para as queues principais
    @Bean
    public Binding investmentCreatedBinding(Queue investmentCreatedQueue, TopicExchange investmentExchange) {
        return BindingBuilder.bind(investmentCreatedQueue)
                .to(investmentExchange)
                .with(investmentCreatedRoutingKey);
    }

    @Bean
    public Binding investmentWithdrawnBinding(Queue investmentWithdrawnQueue, TopicExchange investmentExchange) {
        return BindingBuilder.bind(investmentWithdrawnQueue)
                .to(investmentExchange)
                .with(investmentWithdrawnRoutingKey);
    }

    // Binding para a DLQ
    @Bean
    public Binding investmentNotificationDlqBinding(Queue investmentNotificationDlq, TopicExchange investmentExchange) {
        return BindingBuilder.bind(investmentNotificationDlq)
                .to(investmentExchange)
                .with(investmentNotificationDlqRoutingKey);
    }

    // Configuração do Message Converter para JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Configuração do RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        // Configurações adicionais para confiabilidade
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setReceiveTimeout(60000);

        return rabbitTemplate;
    }

 }