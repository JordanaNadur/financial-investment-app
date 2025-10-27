package com.financial.investment.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Responsável por enviar eventos de investimento ao RabbitMQ.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InvestmentEventProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.investment:investment.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routingkey.investment-created:investment.created}")
    private String routingKeyCreated;

    @Value("${rabbitmq.routingkey.investment-withdrawn:investment.withdrawn}")
    private String routingKeyWithdrawn;

    public void sendInvestmentCreatedEvent(InvestmentCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKeyCreated, event);
            log.info("✅ [RabbitMQ] InvestmentCreatedEvent publicado: {}", event);
        } catch (Exception e) {
            log.error("❌ [RabbitMQ] Falha ao publicar InvestmentCreatedEvent: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao publicar evento de criação de investimento", e);
        }
    }

    public void sendInvestmentWithdrawnEvent(InvestmentWithdrawnEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKeyWithdrawn, event);
            log.info("✅ [RabbitMQ] InvestmentWithdrawnEvent publicado: {}", event);
        } catch (Exception e) {
            log.error("❌ [RabbitMQ] Falha ao publicar InvestmentWithdrawnEvent: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao publicar evento de resgate de investimento", e);
        }
    }
}
