package com.financial.catalog.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    //Tipo do produto (Ação, CDB, Fundo...)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    //Rentabilidade mensal estimada
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal monthlyReturn;

    //Prazo mínimo de investimento em meses
    @Column(nullable = false)
    private Integer minimumTermMonths;

    //Nível de risco: Baixo, Médio, Alto
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;

    //Valor mínimo de investimento
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumInvestment;

    //Descrição do produto
    private String description;

    //Produto disponível
    @Column(nullable = false)
    private Boolean active = true;

}
