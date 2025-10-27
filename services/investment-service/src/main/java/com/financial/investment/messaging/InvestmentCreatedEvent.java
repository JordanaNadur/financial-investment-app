package com.financial.investment.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento publicado quando um investimento é criado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentCreatedEvent {
    private Long userId;
    private Long investmentId;
    private BigDecimal amount;
    private Integer termInMonths;
    private BigDecimal monthlyReturn;
    private String modality;
    private LocalDateTime createdAt;
    private String message;
}