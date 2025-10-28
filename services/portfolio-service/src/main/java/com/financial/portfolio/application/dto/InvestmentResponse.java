package com.financial.portfolio.application.dto;

import com.financial.portfolio.domain.InvestmentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvestmentResponse {

    private Long id;
    private String name;
    private InvestmentType type;
    private BigDecimal amount;
    private BigDecimal unitPrice;
    private BigDecimal totalValue;
    private Long portfolioId;

}

