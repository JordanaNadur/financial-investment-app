package com.financial.investment.domain.usecase;

import com.financial.investment.domain.Investment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculateReturnUseCase {

    public BigDecimal execute(Investment investment) {
        LocalDate now = LocalDate.now();
        long monthsElapsed = ChronoUnit.MONTHS.between(investment.getDataInvestimento(), now);
        if (monthsElapsed < 0) monthsElapsed = 0;

        BigDecimal rate = investment.getRentabilidadeMensal();
        BigDecimal principal = investment.getValor();

        // Rentabilidade composta mensal: valor * (1 + rate)^meses
        BigDecimal factor = BigDecimal.ONE.add(rate).pow((int) monthsElapsed);
        BigDecimal totalValue = principal.multiply(factor).setScale(2, RoundingMode.HALF_UP);

        return totalValue.subtract(principal); // Apenas os juros
    }
}
