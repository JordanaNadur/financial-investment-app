package com.financial.investment.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageFactory {

    private final MessageConverter messageConverter = new SimpleMessageConverter();

    public Message createJsonMessage(Object event, String eventType) {
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setHeader("eventType", eventType);
        properties.setHeader("source", "financial-investment-service");
        properties.setHeader("timestamp", System.currentTimeMillis());

        return messageConverter.toMessage(event, properties);
    }
}
