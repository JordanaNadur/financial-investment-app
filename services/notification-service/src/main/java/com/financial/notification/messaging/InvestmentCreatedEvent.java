package com.financial.notification.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentCreatedEvent {
    private Long investmentId;
    private Long userId;
    private String modality;
    private Double amount;
    private Integer termInMonths;
    private BigDecimal monthlyReturn;
}