package com.financial.investment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentRequest {

    @NotNull(message = "valor é obrigatório")
    @DecimalMin(value = "0.01", message = "valor deve ser maior que zero")
    private BigDecimal valor;                 // Valor do investimento

    @NotNull(message = "prazoMeses é obrigatório")
    @Min(value = 1, message = "prazoMeses deve ser no mínimo 1")
    private Integer prazoMeses;               // Prazo do investimento em meses

    @NotNull(message = "rentabilidadeMensal é obrigatória")
    @DecimalMin(value = "0.0", inclusive = true, message = "rentabilidadeMensal não pode ser negativa")
    private BigDecimal rentabilidadeMensal;   // Rentabilidade mensal prevista

    @NotBlank(message = "modalidade é obrigatória")
    private String modalidade;                // Modalidade do investimento (ex: "MENSAL" ou "ANUAL")

    @NotNull(message = "optionId é obrigatório")
    @Positive(message = "optionId deve ser positivo")
    private Long optionId;                    // ID da opção de investimento do catálogo
}
