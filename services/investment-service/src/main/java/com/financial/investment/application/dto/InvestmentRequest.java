package com.financial.investment.application.dto;

import com.financial.investment.domain.Investment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvestmentRequest {

    @NotNull
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull
    @Min(value = 1, message = "Prazo deve ser pelo menos 1 mês")
    private Integer prazoMeses;

    @NotNull
    @DecimalMin(value = "0.0", message = "Rentabilidade mensal deve ser não negativa")
    private BigDecimal rentabilidadeMensal;

    @NotNull
    private Investment.TipoModalidade modalidade;

    public InvestmentRequest() {}

    public InvestmentRequest(BigDecimal valor, Integer prazoMeses, BigDecimal rentabilidadeMensal, Investment.TipoModalidade modalidade) {
        this.valor = valor;
        this.prazoMeses = prazoMeses;
        this.rentabilidadeMensal = rentabilidadeMensal;
        this.modalidade = modalidade;
    }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Integer getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(Integer prazoMeses) { this.prazoMeses = prazoMeses; }

    public BigDecimal getRentabilidadeMensal() { return rentabilidadeMensal; }
    public void setRentabilidadeMensal(BigDecimal rentabilidadeMensal) { this.rentabilidadeMensal = rentabilidadeMensal; }

    public Investment.TipoModalidade getModalidade() { return modalidade; }
    public void setModalidade(Investment.TipoModalidade modalidade) { this.modalidade = modalidade; }
}
