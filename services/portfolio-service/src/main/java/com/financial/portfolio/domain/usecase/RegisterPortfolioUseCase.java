package com.financial.portfolio.domain.usecase;

import com.financial.portfolio.domain.Portfolio;
import com.financial.portfolio.domain.port.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisterPortfolioUseCase {

    private final PortfolioRepository portfolioRepository;

    public Optional<Portfolio> getPortfolioByName(String name){
        return portfolioRepository.findByName(name);
    }
    

}
