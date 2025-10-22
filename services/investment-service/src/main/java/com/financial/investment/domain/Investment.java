package com.financial.investment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "investments")
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull
    @Min(value = 1, message = "Prazo deve ser pelo menos 1 mês")
    private Integer prazoMeses;

    @NotNull
    @DecimalMin(value = "0.0", message = "Rentabilidade mensal deve ser não negativa")
    private BigDecimal rentabilidadeMensal; // ex: 0.008 para 0.8%

    @NotNull
    private LocalDate dataInvestimento;

    @Enumerated(EnumType.STRING)
    private TipoModalidade modalidade; // ANUAL, MENSAL

    public enum TipoModalidade {
        MENSAL, ANUAL
    }

    // Constructors, getters, setters
    public Investment() {}

    public Investment(Long userId, BigDecimal valor, Integer prazoMeses, BigDecimal rentabilidadeMensal, TipoModalidade modalidade) {
        this.userId = userId;
        this.valor = valor;
        this.prazoMeses = prazoMeses;
        this.rentabilidadeMensal = rentabilidadeMensal;
        this.modalidade = modalidade;
        this.dataInvestimento = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Integer getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(Integer prazoMeses) { this.prazoMeses = prazoMeses; }

    public BigDecimal getRentabilidadeMensal() { return rentabilidadeMensal; }
    public void setRentabilidadeMensal(BigDecimal rentabilidadeMensal) { this.rentabilidadeMensal = rentabilidadeMensal; }

    public LocalDate getDataInvestimento() { return dataInvestimento; }
    public void setDataInvestimento(LocalDate dataInvestimento) { this.dataInvestimento = dataInvestimento; }

    public TipoModalidade getModalidade() { return modalidade; }
    public void setModalidade(TipoModalidade modalidade) { this.modalidade = modalidade; }
}
