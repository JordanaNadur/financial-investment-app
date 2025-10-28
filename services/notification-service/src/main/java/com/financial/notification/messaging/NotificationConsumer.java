package com.financial.notification.messaging;

import com.financial.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.investment-created}")
    @Transactional
    public void handleInvestmentCreated(InvestmentCreatedEvent event) {
        try {
            log.info("Received investment created event: userId={}, investmentId={}",
                    event.getUserId(), event.getInvestmentId());

            // Processar e salvar a notificação no banco de dados
            notificationService.processInvestmentCreatedNotification(event);

            log.info("Successfully processed investment created event for investment ID: {}",
                    event.getInvestmentId());

        } catch (Exception e) {
            log.error("Error processing investment created event for investment ID {}: {}",
                    event.getInvestmentId(), e.getMessage(), e);
            // Em produção, considerar DLQ (Dead Letter Queue)
            throw e; // Re-throw para fazer retry ou mover para DLQ
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.investment-withdrawn}")
    @Transactional
    public void handleInvestmentWithdrawn(InvestmentWithdrawnEvent event) {
        try {
            log.info("Received investment withdrawn event: userId={}, investmentId={}",
                    event.getUserId(), event.getInvestmentId());

            // Processar e salvar a notificação de resgate
            notificationService.processInvestmentWithdrawnNotification(event);

            log.info("Successfully processed investment withdrawn event for investment ID: {}",
                    event.getInvestmentId());

        } catch (Exception e) {
            log.error("Error processing investment withdrawn event for investment ID {}: {}",
                    event.getInvestmentId(), e.getMessage(), e);
            throw e;
        }
    }
}