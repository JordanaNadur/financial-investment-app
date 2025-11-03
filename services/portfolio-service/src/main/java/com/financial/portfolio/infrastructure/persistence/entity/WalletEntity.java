package com.financial.portfolio.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {

    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;
}
