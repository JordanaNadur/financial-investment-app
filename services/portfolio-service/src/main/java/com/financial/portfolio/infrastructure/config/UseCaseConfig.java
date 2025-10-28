package com.financial.portfolio.infrastructure.config;

import com.financial.portfolio.domain.port.InvestmentRepository;
import com.financial.portfolio.domain.port.PortfolioRepository;
import com.financial.portfolio.domain.usecase.RegisterInvestmentUseCase;
import com.financial.portfolio.domain.usecase.RegisterPortfolioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegisterPortfolioUseCase registerPortfolioUseCase(PortfolioRepository portfolioRepository) {
        return new RegisterPortfolioUseCase(portfolioRepository);
    }

    @Bean
    public RegisterInvestmentUseCase registerInvestmentUseCase(
            InvestmentRepository investmentRepository,
            PortfolioRepository portfolioRepository) {
        return new RegisterInvestmentUseCase(investmentRepository, portfolioRepository);
    }

}

