package com.financial.portfolio.domain.usecase;

import com.financial.portfolio.domain.Investment;
import com.financial.portfolio.domain.InvestmentType;
import com.financial.portfolio.domain.Portfolio;
import com.financial.portfolio.domain.port.InvestmentRepository;
import com.financial.portfolio.domain.port.PortfolioRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class RegisterInvestmentUseCase {

    private final InvestmentRepository investmentRepository;
    private final PortfolioRepository portfolioRepository;

    public Optional<Investment> getInvestmentByName(String name) {
        return investmentRepository.findByName(name);
    }

    public Optional<Investment> getInvestmentById(Long id) {
        return investmentRepository.findById(id);
    }

    public Investment registerInvestment(String name, InvestmentType type, BigDecimal amount, 
                                        BigDecimal unitPrice, Long portfolioId) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(portfolioId);
        
        if (portfolioOptional.isEmpty()) {
            throw new IllegalArgumentException("Portfolio não encontrado com ID: " + portfolioId);
        }

        Investment investment = new Investment();
        investment.setName(name);
        investment.setType(type);
        investment.setAmount(amount);
        investment.setUnitPrice(unitPrice);
        investment.setTotalValue(amount.multiply(unitPrice));
        investment.setPortfolio(portfolioOptional.get());

        return investmentRepository.save(investment);
    }

    public Optional<Investment> updateInvestment(Long id, String name, InvestmentType type, 
                                                 BigDecimal amount, BigDecimal unitPrice) {
        Optional<Investment> investmentOptional = investmentRepository.findById(id);

        if (investmentOptional.isPresent()) {
            Investment investment = investmentOptional.get();
            investment.setName(name);
            investment.setType(type);
            investment.setAmount(amount);
            investment.setUnitPrice(unitPrice);
            investment.setTotalValue(amount.multiply(unitPrice));
            
            Investment updatedInvestment = investmentRepository.save(investment);
            return Optional.of(updatedInvestment);
        }

        return Optional.empty();
    }

    public boolean deleteInvestment(Long id) {
        if (investmentRepository.existsById(id)) {
            investmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

