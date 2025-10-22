package com.financial.investment.domain.usecase;

import com.financial.investment.domain.Investment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class WithdrawInvestmentUseCase {

    private final CalculateReturnUseCase calculateReturnUseCase;

    public WithdrawInvestmentUseCase(CalculateReturnUseCase calculateReturnUseCase) {
        this.calculateReturnUseCase = calculateReturnUseCase;
    }

    public BigDecimal execute(Investment investment) {
        LocalDate now = LocalDate.now();
        long monthsElapsed = ChronoUnit.MONTHS.between(investment.getDataInvestimento(), now);
        long totalMonths = investment.getPrazoMeses();

        BigDecimal principal = investment.getValor();
        BigDecimal returns = calculateReturnUseCase.execute(investment);

        if (monthsElapsed < totalMonths) {
            // Penalidade: reduzir 10% dos juros
            returns = returns.multiply(BigDecimal.valueOf(0.9)).setScale(2, RoundingMode.HALF_UP);
        }

        return principal.add(returns);
    }
}
