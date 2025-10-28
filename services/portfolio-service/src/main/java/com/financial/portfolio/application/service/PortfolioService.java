package com.financial.portfolio.application.service;

import com.financial.portfolio.application.dto.PortfolioRegisterRequest;
import com.financial.portfolio.application.dto.PortfolioResponse;
import com.financial.portfolio.application.dto.PortfolioUpdateRequest;
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
                .orElseThrow(() -> new Exception("Portfolio não encontrado!"));

        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .clientId(portfolio.getClientId())
                .totalValue(portfolio.getTotalValue())
                .createdAt(portfolio.getCreatedAt())
                .build();

    }

    public PortfolioResponse registerPortfolio(PortfolioRegisterRequest request) {

        Portfolio portfolio = registerPortfolioUseCase.registerPortfolio(
                request.getName(),
                request.getClientId()
        );

        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .clientId(portfolio.getClientId())
                .totalValue(portfolio.getTotalValue())
                .createdAt(portfolio.getCreatedAt())
                .build();
    }

    public PortfolioResponse updatePortfolio(Long id, PortfolioUpdateRequest request) throws Exception {

        Portfolio portfolio = registerPortfolioUseCase.updatePortfolio(id, request.getName())
                .orElseThrow(() -> new Exception("Portfolio não encontrado!"));

        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .clientId(portfolio.getClientId())
                .totalValue(portfolio.getTotalValue())
                .createdAt(portfolio.getCreatedAt())
                .build();
    }

    public void deletePortfolio(Long id) throws Exception {
        boolean deleted = registerPortfolioUseCase.deletePortfolio(id);
        
        if(!deleted){
            throw new Exception("Portfolio não encontrado!");
        }
    }

}
