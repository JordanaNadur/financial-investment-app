package com.financial.investment.application.service;

import com.financial.investment.application.dto.InvestmentRequest;
import com.financial.investment.application.dto.InvestmentResponse;
import com.financial.investment.application.dto.TransactionWithdrawRequest;
import com.financial.investment.application.dto.TransactionInvestRequest;
import com.financial.investment.infrastructure.persistence.entity.InvestmentEntity;
import com.financial.investment.infrastructure.persistence.mapper.InvestmentMapper;
import com.financial.investment.infrastructure.persistence.repository.InvestmentRepository;
import com.financial.investment.infrastructure.client.TransactionClient;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InvestmentService {

    private final InvestmentEventProducer investmentEventProducer;
    private final InvestmentRepository investmentRepository;
    private final TransactionClient transactionClient;
    private final com.financial.investment.infrastructure.client.CatalogClient catalogClient;

    public InvestmentResponse createInvestment(Long userId, InvestmentRequest request) {
        try {
        // Primeiro garante o APORTE (debitando a wallet) no transaction-service
        TransactionInvestRequest investReq = new TransactionInvestRequest(userId, request.getOptionId(), request.getValor());
        transactionClient.registerInvest(investReq); // se falhar, lança exceção e nada é persistido

        // Persistir investimento somente após sucesso no débito
        // Buscar dados do produto para armazenar snapshot (nome/tipo)
        var product = catalogClient.getProduct(request.getOptionId());

        InvestmentEntity entity = InvestmentEntity.builder()
            .userId(userId)
            .optionId(request.getOptionId())
            .amount(request.getValor())
            .monthlyReturn(request.getRentabilidadeMensal())
            .termInMonths(request.getPrazoMeses())
            .modality(request.getModalidade())
            .productName(product != null ? product.name() : null)
            .productType(product != null ? product.type() : null)
            .build();
        InvestmentEntity saved = investmentRepository.save(entity);
        Long investmentId = saved.getId();
            BigDecimal simulatedReturn = calculateReturn(
                    request.getValor(),
                    request.getRentabilidadeMensal(),
                    request.getPrazoMeses()
            );

            InvestmentCreatedEvent event = InvestmentCreatedEvent.builder()
                    .userId(userId)
                    .investmentId(investmentId)
                    // .optionId(request.getOptionId()) // alinhar se o evento suportar esse campo
                    .amount(request.getValor())
                    .termInMonths(request.getPrazoMeses())
                    .monthlyReturn(request.getRentabilidadeMensal())
                    .modality(request.getModalidade())
            .createdAt(saved.getCreatedAt() != null ? saved.getCreatedAt() : LocalDateTime.now())
                    .message("Novo investimento criado: " + request.getModalidade())
                    .build();

            investmentEventProducer.sendInvestmentCreatedEvent(event);

        log.info("💰 Investimento criado e persistido. ID: {}, Usuário: {}", investmentId, userId);

        return new InvestmentResponse(
            investmentId,
            request.getValor(),
            simulatedReturn,
            request.getModalidade(),
            product != null ? product.name() : null,
            product != null ? product.type() : null,
            (saved.getCreatedAt() != null ? saved.getCreatedAt() : LocalDateTime.now()),
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
            // Calcular valor de resgate: principal + ganho proporcional (usando monthsElapsed)
            // Localizar o investimento e validar status antes de publicar evento
            InvestmentEntity entity = investmentRepository.findByIdAndUserId(investmentId, userId)
                    .orElseThrow(() -> new IllegalArgumentException("Investimento não encontrado."));
            if (Boolean.FALSE.equals(entity.getIsActive())) {
                throw new IllegalArgumentException("Investimento já foi sacado/desativado.");
            }
            int monthsElapsed = Math.max(0, Math.min(entity.getTermInMonths(), java.time.Period.between(entity.getCreatedAt().toLocalDate(), LocalDateTime.now().toLocalDate()).getMonths() + 12 * (LocalDateTime.now().getYear() - entity.getCreatedAt().getYear())));
            BigDecimal ganho = calculateReturn(entity.getAmount(), entity.getMonthlyReturn(), monthsElapsed);
            BigDecimal withdrawnAmount = entity.getAmount().add(ganho).setScale(2, java.math.RoundingMode.HALF_UP);
            // Chamar transaction-service (/transactions/withdraw) para registrar transação de SAQUE e creditar carteira
            Long optionId = entity.getOptionId();
            TransactionWithdrawRequest txReq = new TransactionWithdrawRequest(userId, optionId, withdrawnAmount);
            transactionClient.registerWithdraw(txReq); // se falhar, não desativa o investimento

            // Após creditar com sucesso, desativar e persistir
            entity.setIsActive(false);
            entity.setWithdrawnAt(LocalDateTime.now());
            investmentRepository.save(entity);
            log.info("🔒 Investimento desativado após saque. ID: {}, Usuário: {}", investmentId, userId);

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
        Page<InvestmentEntity> investments = investmentRepository.findByUserIdAndIsActiveTrue(userId, pageable);
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
        List<InvestmentEntity> investments = investmentRepository.findByUserIdAndIsActiveTrue(userId);
        log.info("📊 Retornando {} investimentos para usuário {}", investments.size(), userId);
        return investments.stream()
                .map(InvestmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateReturn(BigDecimal valor, BigDecimal rentabilidadeMensal, int prazoMeses) {
        BigDecimal taxaMensal = rentabilidadeMensal.add(BigDecimal.ONE);
        BigDecimal valorFuturo = valor.multiply(taxaMensal.pow(prazoMeses));
    return valorFuturo.subtract(valor).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}