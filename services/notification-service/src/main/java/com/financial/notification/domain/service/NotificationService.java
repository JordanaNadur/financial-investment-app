package com.financial.notification.domain.service;

import com.financial.notification.domain.model.Investment;
import com.financial.notification.domain.model.port.InvestmentRepository;
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
            log.info("Processing investment created notification for investment ID: {}", event.getInvestmentId());

            Investment investment = createInvestmentCreatedNotification(event);
            Investment savedNotification = investmentRepository.save(investment);

            // Simular envio e atualizar status
            sendNotification(savedNotification);

            log.info("Successfully processed investment created notification for investment ID: {}",
                    event.getInvestmentId());

        } catch (Exception e) {
            log.error("Failed to process investment created notification for investment ID {}: {}",
                    event.getInvestmentId(), e.getMessage(), e);
            throw new RuntimeException("Error processing investment notification", e);
        }
    }

    @Transactional
    public void processInvestmentWithdrawnNotification(InvestmentWithdrawnEvent event) {
        try {
            log.info("Processing investment withdrawn notification for investment ID: {}", event.getInvestmentId());

            Investment investment = createInvestmentWithdrawnNotification(event);
            Investment savedNotification = investmentRepository.save(investment);

            // Simular envio e atualizar status
            sendNotification(savedNotification);

            log.info("Successfully processed investment withdrawn notification for investment ID: {}",
                    event.getInvestmentId());

        } catch (Exception e) {
            log.error("Failed to process investment withdrawn notification for investment ID {}: {}",
                    event.getInvestmentId(), e.getMessage(), e);
            throw new RuntimeException("Error processing withdrawal notification", e);
        }
    }

    private Investment createInvestmentCreatedNotification(InvestmentCreatedEvent event) {
    // Converter amount recebido (Double) para BigDecimal esperado na entidade
    java.math.BigDecimal amount = event.getAmount() != null
        ? java.math.BigDecimal.valueOf(event.getAmount())
        : java.math.BigDecimal.ZERO;

        // Criar InvestmentDetails apenas com os campos que existem
        Investment.InvestmentDetails details = Investment.InvestmentDetails.builder()
                .investmentName(event.getModality() != null ? event.getModality() : "Unknown Investment")
                .modality(event.getModality())
                .termInMonths(event.getTermInMonths())
                // Remover campos que não existem: .amount() e outros
                .build();

        String message = String.format(
                "Seu investimento '%s' foi criado com sucesso! " +
                        "Valor: R$ %.2f, Prazo: %d meses",
                event.getModality() != null ? event.getModality() : "Unknown Investment",
                event.getAmount() != null ? event.getAmount() : 0.0,
                event.getTermInMonths() != null ? event.getTermInMonths() : 0
        );

    return Investment.builder()
                .userId(event.getUserId())
                .amount(amount)
        .monthlyReturn(event.getMonthlyReturn() != null ? event.getMonthlyReturn() : java.math.BigDecimal.ZERO)
                .investmentId(event.getInvestmentId())
                .type(Investment.InvestmentType.INVESTMENT_CREATED)
                .title("Novo Investimento Criado")
                .message(message)
                .status(Investment.InvestmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .investmentDetails(details)
                .build();
    }

    private Investment createInvestmentWithdrawnNotification(InvestmentWithdrawnEvent event) {
        // Criar InvestmentDetails apenas com os campos que existem
        Investment.InvestmentDetails details = Investment.InvestmentDetails.builder()
                .investmentName("Investment Withdrawal")
                // Remover campos que não existem: .withdrawnAmount()
                .build();

        String message = String.format(
                "Resgate do investimento realizado com sucesso! Valor resgatado: R$ %.2f",
                event.getWithdrawnAmount() != null ? event.getWithdrawnAmount() : 0.0
        );

        return Investment.builder()
                .userId(event.getUserId())
                .investmentId(event.getInvestmentId())
                .type(Investment.InvestmentType.INVESTMENT_WITHDRAWN)
                .title("Resgate de Investimento")
                .message(message)
                .status(Investment.InvestmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .investmentDetails(details)
                .build();
    }

    private void sendNotification(Investment investment) {
        try {
            // Simular lógica de envio (email, push, SMS, etc.)
            log.info("Sending notification to user {}: {}", investment.getUserId(), investment.getMessage());

            // Atualizar status para enviado
            investment.setStatus(Investment.InvestmentStatus.SENT);
            investment.setSentAt(LocalDateTime.now());
            investmentRepository.save(investment);

        } catch (Exception e) {
            log.error("Failed to send notification for investment ID {}: {}",
                    investment.getInvestmentId(), e.getMessage());
            investment.setStatus(Investment.InvestmentStatus.FAILED);
            investmentRepository.save(investment);
            throw e;
        }
    }
}