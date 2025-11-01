package com.financial.catalog.application.dto;

import java.math.BigDecimal;

import com.financial.catalog.domain.ProductType;
import com.financial.catalog.domain.RiskLevel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private ProductType type;
    private String description;
    private RiskLevel riskLevel;
    private BigDecimal minimumInvestment;
    private BigDecimal monthlyReturn;
    private Integer minimumTermMonths;
    private Boolean active;
}
