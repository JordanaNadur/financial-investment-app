package com.financial.transaction.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Transaction(Long clientId, Long optionId, BigDecimal amount, TransactionType type) {
        this.clientId = clientId;
        this.optionId = optionId;
        this.amount = amount;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    public enum TransactionType {
        APORTE, RESGATE
    }
}
