package com.financial.notification.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentWithdrawnEvent {
    private Long investmentId;
    private Long userId;
    private Double withdrawnAmount;
    private LocalDateTime withdrawnAt;
}