package com.financial.transaction.application.dto;

import com.financial.transaction.domain.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long clientId;
    private Long optionId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime createdAt;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.clientId = transaction.getClientId();
        this.optionId = transaction.getOptionId();
        this.amount = transaction.getAmount();
        this.type = transaction.getType().name();
        this.createdAt = transaction.getCreatedAt();
    }

    public TransactionResponse(Long id, Long clientId, Long optionId, BigDecimal amount, String type) {
        this.id = id;
        this.clientId = clientId;
        this.optionId = optionId;
        this.amount = amount;
        this.type = type;
    }

    public TransactionResponse(long id, long clientId, BigDecimal amount, String status) {
        this.id = id;
        this.clientId = clientId;
        this.amount = amount;
        this.type = status;
    }
}
