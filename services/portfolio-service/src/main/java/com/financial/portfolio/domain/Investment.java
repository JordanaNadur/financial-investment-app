package com.financial.portfolio.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "investment")
@Data
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // nome do ativo
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentType type; // tipo do ativo
    
    private BigDecimal amount; // quantidade adquirida
    private BigDecimal unitPrice; // preço unitário
    private BigDecimal totalValue; // amount * unitPrice

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

}
