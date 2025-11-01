package com.financial.investment.infrastructure.persistence.mapper;

import com.financial.investment.application.dto.InvestmentResponse;
import com.financial.investment.infrastructure.persistence.entity.InvestmentEntity;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public class InvestmentMapper {

    public static InvestmentResponse toResponse(InvestmentEntity entity) {
        BigDecimal simulatedReturn = calculateReturn(
                entity.getAmount(),
                entity.getMonthlyReturn(),
                entity.getTermInMonths()
        );

        return new InvestmentResponse(
                entity.getId(),
                entity.getAmount(),
                simulatedReturn,
                entity.getModality(),
                entity.getCreatedAt(),
                entity.getWithdrawnAt(),
                entity.getIsActive()
        );
    }

    private static BigDecimal calculateReturn(BigDecimal valor, BigDecimal rentabilidadeMensal, int prazoMeses) {
        BigDecimal taxaMensal = rentabilidadeMensal.add(BigDecimal.ONE);
        BigDecimal valorFuturo = valor.multiply(taxaMensal.pow(prazoMeses));
        return valorFuturo.subtract(valor).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static Page<InvestmentResponse> toResponsePage(Page<InvestmentEntity> entities) {
        return entities.map(InvestmentMapper::toResponse);
    }
}