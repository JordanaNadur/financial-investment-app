package com.financial.portfolio.application.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PortfolioResponse {

    private Long id;
    private String name;
    private Long clientId;
    private BigDecimal totalValue;
    private LocalDate createdAt;

}
