package com.financial.portfolio.application.service;

import com.financial.portfolio.application.dto.InvestmentRegisterRequest;
import com.financial.portfolio.application.dto.InvestmentResponse;
import com.financial.portfolio.application.dto.InvestmentUpdateRequest;
import com.financial.portfolio.domain.Investment;
import com.financial.portfolio.domain.usecase.RegisterInvestmentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final RegisterInvestmentUseCase registerInvestmentUseCase;

    public InvestmentResponse getInvestmentByName(String name) throws Exception {
        Investment investment = registerInvestmentUseCase.getInvestmentByName(name)
                .orElseThrow(() -> new Exception("Investment não encontrado!"));

        return buildResponse(investment);
    }

    public InvestmentResponse getInvestmentById(Long id) throws Exception {
        Investment investment = registerInvestmentUseCase.getInvestmentById(id)
                .orElseThrow(() -> new Exception("Investment não encontrado!"));

        return buildResponse(investment);
    }

    public InvestmentResponse registerInvestment(InvestmentRegisterRequest request) {
        Investment investment = registerInvestmentUseCase.registerInvestment(
                request.getName(),
                request.getType(),
                request.getAmount(),
                request.getUnitPrice(),
                request.getPortfolioId()
        );

        return buildResponse(investment);
    }

    public InvestmentResponse updateInvestment(Long id, InvestmentUpdateRequest request) throws Exception {
        Investment investment = registerInvestmentUseCase.updateInvestment(
                id,
                request.getName(),
                request.getType(),
                request.getAmount(),
                request.getUnitPrice()
        ).orElseThrow(() -> new Exception("Investment não encontrado!"));

        return buildResponse(investment);
    }

    public void deleteInvestment(Long id) throws Exception {
        boolean deleted = registerInvestmentUseCase.deleteInvestment(id);

        if (!deleted) {
            throw new Exception("Investment não encontrado!");
        }
    }

    private InvestmentResponse buildResponse(Investment investment) {
        return InvestmentResponse.builder()
                .id(investment.getId())
                .name(investment.getName())
                .type(investment.getType())
                .amount(investment.getAmount())
                .unitPrice(investment.getUnitPrice())
                .totalValue(investment.getTotalValue())
                .portfolioId(investment.getPortfolio() != null ? investment.getPortfolio().getId() : null)
                .build();
    }

}

