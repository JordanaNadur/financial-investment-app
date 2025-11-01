package com.financial.investment.application.service;

import com.financial.investment.application.dto.InvestmentRequest;
import com.financial.investment.application.dto.InvestmentResponse;
import com.financial.investment.infrastructure.persistence.entity.InvestmentEntity;
import com.financial.investment.infrastructure.persistence.mapper.InvestmentMapper;
import com.financial.investment.infrastructure.persistence.repository.InvestmentRepository;
import com.financial.investment.messaging.InvestmentCreatedEvent;
import com.financial.investment.messaging.InvestmentEventProducer;
import com.financial.investment.messaging.InvestmentWithdrawnEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InvestmentService {

    private final InvestmentEventProducer investmentEventProducer;
    private final InvestmentRepository investmentRepository; // APENAS para consultas
    private final AtomicLong idCounter = new AtomicLong(1);

    public InvestmentResponse createInvestment(Long userId, InvestmentRequest request) {
        try {
            Long investmentId = idCounter.getAndIncrement();
            BigDecimal simulatedReturn = calculateReturn(
                    request.getValor(),
                    request.getRentabilidadeMensal(),
                    request.getPrazoMeses()
            );

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

            return new InvestmentResponse(
                    investmentId,
                    request.getValor(),
                    simulatedReturn,
                    request.getModalidade(),
                    LocalDateTime.now(),
                    null,
                    true
            );

        } catch (Exception e) {
            log.error("Erro ao criar investimento para o usuário {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Falha ao criar investimento", e);
        }
    }

    public BigDecimal withdrawInvestment(Long userId, Long investmentId) {
        try {
            BigDecimal withdrawnAmount = new BigDecimal("1500.00"); // valor simulado

            InvestmentWithdrawnEvent event = InvestmentWithdrawnEvent.builder()
                    .userId(userId)
                    .investmentId(investmentId)
                    .withdrawnAmount(withdrawnAmount)
                    .withdrawnAt(LocalDateTime.now())
                    .message("Resgate de investimento simulado realizado")
                    .build();

            investmentEventProducer.sendInvestmentWithdrawnEvent(event);

            log.info("🏦 Resgate de investimento simulado publicado. ID: {}, Usuário: {}", investmentId, userId);
            return withdrawnAmount;

        } catch (Exception e) {
            log.error("Erro ao publicar resgate de investimento {}: {}", investmentId, e.getMessage(), e);
            throw new RuntimeException("Falha ao processar resgate de investimento", e);
        }
    }

    // MÉTODOS DE CONSULTA (usam PostgreSQL)
    @Transactional(readOnly = true)
    public Page<InvestmentResponse> getInvestments(Long userId, Pageable pageable) {
        Page<InvestmentEntity> investments = investmentRepository.findByUserId(userId, pageable);
        log.info("📊 Retornando {} investimentos para usuário {}", investments.getNumberOfElements(), userId);
        return InvestmentMapper.toResponsePage(investments);
    }

    @Transactional(readOnly = true)
    public Optional<InvestmentResponse> getInvestment(Long userId, Long id) {
        Optional<InvestmentEntity> investment = investmentRepository.findByIdAndUserId(id, userId);
        log.info("🔍 Buscando investimento ID: {} para usuário: {}", id, userId);
        return investment.map(InvestmentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<InvestmentResponse> getInvestments(Long userId) {
        List<InvestmentEntity> investments = investmentRepository.findByUserId(userId);
        log.info("📊 Retornando {} investimentos para usuário {}", investments.size(), userId);
        return investments.stream()
                .map(InvestmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateReturn(BigDecimal valor, BigDecimal rentabilidadeMensal, int prazoMeses) {
        BigDecimal taxaMensal = rentabilidadeMensal.add(BigDecimal.ONE);
        BigDecimal valorFuturo = valor.multiply(taxaMensal.pow(prazoMeses));
        return valorFuturo.subtract(valor).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}