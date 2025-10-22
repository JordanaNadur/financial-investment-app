package com.financial.investment.messaging;

import com.financial.notification.messaging.InvestmentCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvestmentEventProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;

    public InvestmentEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendInvestmentCreatedEvent(InvestmentCreatedEvent event) {
        rabbitTemplate.convertAndSend(queueName, event);
    }
}
