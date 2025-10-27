package com.financial.notification.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentWithdrawnEvent {
    private Long userId;
    private Long investmentId;
    private BigDecimal withdrawnAmount;
    private LocalDateTime withdrawnAt;
    private String message;
}