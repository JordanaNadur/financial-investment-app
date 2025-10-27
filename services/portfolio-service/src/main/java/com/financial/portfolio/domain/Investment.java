package com.financial.portfolio.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // nome do ativo
    private String type; // tipo do ativo: AÇÃO, FUNDO, TESOURO, CRIPTO, etc.
    private BigDecimal amount; // quantidade adquirida
    private BigDecimal unitPrice; // preço unitário
    private BigDecimal totalValue; // amount * unitPrice

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

}
