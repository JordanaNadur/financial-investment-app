package com.financial.transaction.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private Long clientId;
    private Long optionId;
    private BigDecimal amount;
    private String type; // "APORTE" or "RESGATE"

    public TransactionRequest(Long clientId, BigDecimal amount, String type) {
        this.clientId = clientId;
        this.amount = amount;
        this.type = type;
    }
}
