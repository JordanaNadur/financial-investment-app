package com.financial.notification.domain.service;

import com.financial.notification.domain.model.Investment;
import com.financial.notification.domain.port.InvestmentRepository;
import com.financial.notification.messaging.InvestmentCreatedEvent;
import com.financial.notification.messaging.InvestmentWithdrawnEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final InvestmentRepository investmentRepository;

    @Transactional
    public void processInvestmentCreatedNotification(InvestmentCreatedEvent event) {
        try {
            // Buscar o investment existente ou criar um novo para notificação
            Investment investment = investmentRepository.findById(event.getInvestmentId())
                    .orElseGet(() -> {
                        // Se não existir, criar um novo investment para a notificação
                        return Investment.builder()
                                .id(event.getInvestmentId())
                                .name(event.getModality() != null ? event.getModality() : "Unknown Investment")
                                .amount(event.getAmount())
                                .build();
                    });

            // Atualizar os campos de notificação
            investment.setUserId(event.getUserId());
            investment.setInvestmentId(event.getInvestmentId());
            investment.setType(Investment.InvestmentType.INVESTMENT_CREATED);
            investment.setTitle("Novo Investimento Criado");
            investment.setMessage(buildInvestmentCreatedMessage(event, investment));
            investment.setStatus(Investment.InvestmentStatus.PENDING);
            investment.setCreatedAt(LocalDateTime.now());
            investment.setMetadata(buildInvestmentMetadata(event));

            Investment savedInvestment = investmentRepository.save(investment);
            log.info("Investment notification saved with ID: {} for investment ID: {}",
                    savedInvestment.getId(), event.getInvestmentId());

            // Atualizar status para enviado
            savedInvestment.setStatus(Investment.InvestmentStatus.SENT);
            savedInvestment.setSentAt(LocalDateTime.now());
            investmentRepository.save(savedInvestment);

        } catch (Exception e) {
            log.error("Failed to process investment created notification for investment ID {}: {}",
                    event.getInvestmentId(), e.getMessage(), e);
            throw new RuntimeException("Error processing investment notification", e);
        }
    }

    @Transactional
    public void processInvestmentWithdrawnNotification(InvestmentWithdrawnEvent event) {
        try {
            // Buscar o investment existente
            Investment investment = investmentRepository.findById(event.getInvestmentId())
                    .orElseGet(() -> {
                        return Investment.builder()
                                .id(event.getInvestmentId())
                                .name("Investment Withdrawal")
                                .amount(event.getWithdrawnAmount())
                                .build();
                    });

            // Atualizar para notificação de resgate
            investment.setUserId(event.getUserId());
            investment.setInvestmentId(event.getInvestmentId());
            investment.setType(Investment.InvestmentType.INVESTMENT_WITHDRAWN);
            investment.setTitle("Resgate de Investimento");
            investment.setMessage(buildInvestmentWithdrawnMessage(event, investment));
            investment.setStatus(Investment.InvestmentStatus.PENDING);
            investment.setCreatedAt(LocalDateTime.now());
            investment.setMetadata(buildWithdrawnMetadata(event));

            Investment savedInvestment = investmentRepository.save(investment);

            savedInvestment.setStatus(Investment.InvestmentStatus.SENT);
            savedInvestment.setSentAt(LocalDateTime.now());
            investmentRepository.save(savedInvestment);

            log.info("Withdrawal notification processed for investment ID: {}", event.getInvestmentId());

        } catch (Exception e) {
            log.error("Failed to process investment withdrawn notification for investment ID {}: {}",
                    event.getInvestmentId(), e.getMessage(), e);
            throw new RuntimeException("Error processing withdrawal notification", e);
        }
    }

    private String buildInvestmentCreatedMessage(InvestmentCreatedEvent event, Investment investment) {
        return String.format(
                "Seu investimento '%s' foi criado com sucesso! " +
                        "Valor: R$ %.2f",
                investment.getName(),
                investment.getAmount() != null ? investment.getAmount() : 0.0
        );
    }

    private String buildInvestmentWithdrawnMessage(InvestmentWithdrawnEvent event, Investment investment) {
        return String.format(
                "Resgate do investimento '%s' realizado com sucesso! Valor resgatado: R$ %.2f",
                investment.getName(),
                event.getWithdrawnAmount()
        );
    }

    private String buildInvestmentMetadata(InvestmentCreatedEvent event) {
        return String.format(
                "{\"investmentId\": %d, \"modality\": \"%s\"}",
                event.getInvestmentId(),
                event.getModality() != null ? event.getModality() : "N/A"
        );
    }

    private String buildWithdrawnMetadata(InvestmentWithdrawnEvent event) {
        return String.format(
                "{\"withdrawnAmount\": %.2f, \"investmentId\": %d}",
                event.getWithdrawnAmount(),
                event.getInvestmentId()
        );
    }
}