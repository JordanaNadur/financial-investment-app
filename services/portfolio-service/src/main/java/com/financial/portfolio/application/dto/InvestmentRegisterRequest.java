package com.financial.portfolio.application.dto;

import com.financial.portfolio.domain.InvestmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestmentRegisterRequest {

    @NotBlank(message = "Nome do ativo é obrigatório")
    private String name;

    @NotNull(message = "Tipo do ativo é obrigatório")
    private InvestmentType type;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private BigDecimal amount;

    @NotNull(message = "Preço unitário é obrigatório")
    @Positive(message = "Preço unitário deve ser positivo")
    private BigDecimal unitPrice;

    @NotNull(message = "ID do portfolio é obrigatório")
    private Long portfolioId;

}

