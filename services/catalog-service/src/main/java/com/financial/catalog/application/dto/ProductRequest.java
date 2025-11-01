package com.financial.catalog.application.dto;

import java.math.BigDecimal;

import com.financial.catalog.domain.ProductType;
import com.financial.catalog.domain.RiskLevel;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    @NotNull(message = "O tipo do produto é obrigatório")
    private ProductType type;

    @NotBlank(message = "A descrição do produto é obrigatório")
    private String description;

    @NotNull(message = "Nível de risco é obrigatório")
    private RiskLevel riskLevel;

    @NotNull(message = "Investimento mínimo é obrigatório")
    @DecimalMin("0.0")
    private BigDecimal minimumInvestment;

    @NotNull(message = "Retorno mensal é obrigatório")
    @DecimalMin("0.0")
    private BigDecimal monthlyReturn;

    @NotNull(message = "Prazo mínimo é obrigatório")
    @Min(0)
    private Integer minimumTermMonths;

    private Boolean active = true;
}
