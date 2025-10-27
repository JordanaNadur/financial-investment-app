package com.financial.investment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentRequest {

    private BigDecimal valor;                 // Valor do investimento
    private Integer prazoMeses;               // Prazo do investimento em meses
    private BigDecimal rentabilidadeMensal;   // Rentabilidade mensal prevista
    private String modalidade;                // Modalidade do investimento (ex: "MENSAL" ou "ANUAL")
}
