package com.financial.investment.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String INVESTMENT_EXCHANGE = "investment.exchange";
    public static final String INVESTMENT_CREATED_QUEUE = "investment.created.queue";
    public static final String INVESTMENT_WITHDRAWN_QUEUE = "investment.withdrawn.queue";

    public static final String ROUTING_KEY_CREATED = "investment.created";
    public static final String ROUTING_KEY_WITHDRAWN = "investment.withdrawn";

    @Bean
    public TopicExchange investmentExchange() {
        return new TopicExchange(INVESTMENT_EXCHANGE, true, false);
    }

    @Bean
    public Queue investmentCreatedQueue() {
        return new Queue(INVESTMENT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue investmentWithdrawnQueue() {
        return new Queue(INVESTMENT_WITHDRAWN_QUEUE, true);
    }

    @Bean
    public Binding bindingCreated() {
        return BindingBuilder.bind(investmentCreatedQueue())
                .to(investmentExchange())
                .with(ROUTING_KEY_CREATED);
    }

    @Bean
    public Binding bindingWithdrawn() {
        return BindingBuilder.bind(investmentWithdrawnQueue())
                .to(investmentExchange())
                .with(ROUTING_KEY_WITHDRAWN);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setExchange(INVESTMENT_EXCHANGE);
        return template;
    }
}
