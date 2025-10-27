package com.financial.investment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO de resposta retornado após criação ou resgate de investimento.
 */
@Data
@AllArgsConstructor
public class InvestmentResponse {
    private Long investmentId;
    private BigDecimal investedAmount;
    private BigDecimal expectedReturn;
    private String modality;
}
