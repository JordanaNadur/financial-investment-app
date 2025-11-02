package com.financial.investment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String INVESTMENT_EXCHANGE = "investment.exchange";
    // As filas e bindings serão declaradas pelo notification-service (consumidor)

    public static final String ROUTING_KEY_CREATED = "investment.created";
    public static final String ROUTING_KEY_WITHDRAWN = "investment.withdrawn";

    @Bean
    public TopicExchange investmentExchange() {
        return new TopicExchange(INVESTMENT_EXCHANGE, true, false);
    }

    // Nenhuma fila/binding é declarada aqui para evitar conflito de argumentos

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        // Usa o ObjectMapper do Spring Boot (já configurado com JavaTimeModule e ISO-8601)
        return new Jackson2JsonMessageConverter(objectMapper);
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
