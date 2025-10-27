package com.financial.portfolio.application.service;

import com.financial.portfolio.application.dto.PortfolioResponse;
import com.financial.portfolio.domain.Portfolio;
import com.financial.portfolio.domain.usecase.RegisterPortfolioUseCase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final RegisterPortfolioUseCase registerPortfolioUseCase;

    public PortfolioResponse getPortfolioByName(String name) throws Exception {

        Portfolio portfolio = registerPortfolioUseCase.getPortfolioByName(name)
                .orElseThrow(() -> new Exception("Value not found!"));

        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .createdAt(portfolio.getCreatedAt())
                .build();

    }

}
