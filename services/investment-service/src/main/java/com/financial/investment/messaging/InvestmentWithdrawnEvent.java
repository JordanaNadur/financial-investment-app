package com.financial.investment.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento publicado quando um investimento é resgatado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentWithdrawnEvent {
    private Long userId;
    private Long investmentId;
    private BigDecimal withdrawnAmount;
    private LocalDateTime withdrawnAt;
    private String message;
}