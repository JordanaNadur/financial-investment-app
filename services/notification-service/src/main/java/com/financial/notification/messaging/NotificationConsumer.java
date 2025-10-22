package com.financial.notification.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @RabbitListener(queues = "investment.created.queue")
    public void handleInvestmentCreated(InvestmentCreatedEvent event) {
        System.out.println("Notificação: " + event.getMessage() + " para usuário " + event.getUserId());
        // Aqui poderia enviar e-mail, SMS, etc.
    }
}
