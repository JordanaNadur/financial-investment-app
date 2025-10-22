package com.financial.investment.domain.usecase;

import com.financial.investment.domain.Investment;
import com.financial.investment.domain.port.InvestmentRepository;

public class CreateInvestmentUseCase {

    private final InvestmentRepository investmentRepository;

    public CreateInvestmentUseCase(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    public Investment execute(Long userId, Investment investment) {
        investment.setUserId(userId);
        return investmentRepository.save(investment);
    }
}
