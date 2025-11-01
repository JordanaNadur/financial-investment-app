package com.financial.transaction.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;

    public TransactionEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendInvestmentRealizedEvent(InvestmentRealizedEvent event) {
        rabbitTemplate.convertAndSend(queueName, event);
    }
}
