package com.financial.investment.application.service;

import com.financial.investment.application.dto.InvestmentRequest;
import com.financial.investment.application.dto.InvestmentResponse;
import com.financial.investment.messaging.InvestmentCreatedEvent;
import com.financial.investment.messaging.InvestmentEventProducer;
import com.financial.investment.messaging.InvestmentWithdrawnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Serviço de domínio que cria e resgata investimentos,
 * publicando eventos RabbitMQ sem persistir dados em banco.
 */
@Slf4j
@Service
public class InvestmentService {

    private final InvestmentEventProducer investmentEventProducer;
    private final AtomicLong idCounter = new AtomicLong(1);

    public InvestmentService(InvestmentEventProducer investmentEventProducer) {
        this.investmentEventProducer = investmentEventProducer;
    }

    public InvestmentResponse createInvestment(Long userId, InvestmentRequest request) {
        try {
            Long investmentId = idCounter.getAndIncrement();
            BigDecimal simulatedReturn = calculateReturn(request.getValor(), request.getRentabilidadeMensal(), request.getPrazoMeses());

            InvestmentCreatedEvent event = InvestmentCreatedEvent.builder()
                    .userId(userId)
                    .investmentId(investmentId)
                    .amount(request.getValor())
                    .termInMonths(request.getPrazoMeses())
                    .monthlyReturn(request.getRentabilidadeMensal())
                    .modality(request.getModalidade())
                    .createdAt(LocalDateTime.now())
                    .message("Novo investimento criado: " + request.getModalidade())
                    .build();

            investmentEventProducer.sendInvestmentCreatedEvent(event);

            log.info("💰 Novo investimento simulado criado. ID: {}, Usuário: {}", investmentId, userId);
            return new InvestmentResponse(investmentId, request.getValor(), simulatedReturn, request.getModalidade());

        } catch (Exception e) {
            log.error("Erro ao criar investimento para o usuário {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Falha ao criar investimento", e);
        }
    }

    public BigDecimal withdrawInvestment(Long userId, Long investmentId) {
        try {
            BigDecimal withdrawnAmount = new BigDecimal("1000.00"); // valor simulado

            InvestmentWithdrawnEvent event = InvestmentWithdrawnEvent.builder()
                    .userId(userId)
                    .investmentId(investmentId)
                    .withdrawnAmount(withdrawnAmount)
                    .withdrawnAt(LocalDateTime.now())
                    .message("Resgate de investimento realizado")
                    .build();

            investmentEventProducer.sendInvestmentWithdrawnEvent(event);

            log.info("🏦 Resgate de investimento publicado. ID: {}, Usuário: {}", investmentId, userId);
            return withdrawnAmount;

        } catch (Exception e) {
            log.error("Erro ao publicar resgate de investimento {}: {}", investmentId, e.getMessage(), e);
            throw new RuntimeException("Falha ao processar resgate de investimento", e);
        }
    }

    private BigDecimal calculateReturn(BigDecimal valor, BigDecimal rentabilidadeMensal, int prazoMeses) {
        BigDecimal taxaMensal = rentabilidadeMensal.add(BigDecimal.ONE);
        BigDecimal valorFuturo = valor.multiply(taxaMensal.pow(prazoMeses));
        return valorFuturo.subtract(valor).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // Métodos simulados para compatibilidade com o controller

    // Método existente - mantido para compatibilidade
    public List<InvestmentResponse> getInvestments(Long userId) {
        log.warn("⚠️ Serviço em modo 'event-only'. Nenhum investimento persistido.");
        return List.of();
    }

    // Novo método para suportar paginação
    public Page<InvestmentResponse> getInvestments(Long userId, Pageable pageable) {
        log.warn("⚠️ Serviço em modo 'event-only'. Nenhum investimento persistido.");
        return Page.empty(pageable);
    }

    public Optional<InvestmentResponse> getInvestment(Long userId, Long id) {
        log.warn("⚠️ Serviço em modo 'event-only'. Nenhum investimento retornado.");
        return Optional.empty();
    }
}