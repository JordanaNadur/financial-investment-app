package com.financial.investment.domain.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvestmentProjection {
    Long getId();
    Long getUserId();
    BigDecimal getValor();
    Integer getPrazoMeses();
    BigDecimal getRentabilidadeMensal();
    LocalDate getDataInvestimento();
    String getModalidade();

    // Método default para calcular valor futuro
    default BigDecimal getValorFuturo() {
        BigDecimal taxaMensal = getRentabilidadeMensal().add(BigDecimal.ONE);
        return getValor().multiply(taxaMensal.pow(getPrazoMeses()))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // Método default para calcular rentabilidade total
    default BigDecimal getRentabilidadeTotal() {
        return getValorFuturo().subtract(getValor())
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}