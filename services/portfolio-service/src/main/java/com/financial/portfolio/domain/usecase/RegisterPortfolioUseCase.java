package com.financial.portfolio.domain.usecase;

import com.financial.portfolio.domain.Portfolio;
import com.financial.portfolio.domain.port.PortfolioRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class RegisterPortfolioUseCase {

    private final PortfolioRepository portfolioRepository;

    public Optional<Portfolio> getPortfolioByName(String name){
        return portfolioRepository.findByName(name);
    }
    
    public Portfolio registerPortfolio(String name, Long clientId){
        Portfolio portfolio = new Portfolio();
        portfolio.setName(name);
        portfolio.setClientId(clientId);
        portfolio.setTotalValue(BigDecimal.ZERO);
        portfolio.setCreatedAt(LocalDate.now());
        
        return portfolioRepository.save(portfolio);
    }

    public Optional<Portfolio> updatePortfolio(Long id, String name){
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(id);
        
        if(portfolioOptional.isPresent()){
            Portfolio portfolio = portfolioOptional.get();
            portfolio.setName(name);
            Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
            return Optional.of(updatedPortfolio);
        }
        
        return Optional.empty();
    }

    public boolean deletePortfolio(Long id){
        if(portfolioRepository.existsById(id)){
            portfolioRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
