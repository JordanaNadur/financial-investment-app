package com.financial.investment.application.dto;

import com.financial.investment.domain.Investment;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvestmentResponse {

    private Long id;
    private Long userId;
    private BigDecimal valor;
    private Integer prazoMeses;
    private BigDecimal rentabilidadeMensal;
    private LocalDate dataInvestimento;
    private Investment.TipoModalidade modalidade;
    private BigDecimal rentabilidadeAtual;

    public InvestmentResponse() {}

    public InvestmentResponse(Investment investment, BigDecimal rentabilidadeAtual) {
        this.id = investment.getId();
        this.userId = investment.getUserId();
        this.valor = investment.getValor();
        this.prazoMeses = investment.getPrazoMeses();
        this.rentabilidadeMensal = investment.getRentabilidadeMensal();
        this.dataInvestimento = investment.getDataInvestimento();
        this.modalidade = investment.getModalidade();
        this.rentabilidadeAtual = rentabilidadeAtual;
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

    public Investment.TipoModalidade getModalidade() { return modalidade; }
    public void setModalidade(Investment.TipoModalidade modalidade) { this.modalidade = modalidade; }

    public BigDecimal getRentabilidadeAtual() { return rentabilidadeAtual; }
    public void setRentabilidadeAtual(BigDecimal rentabilidadeAtual) { this.rentabilidadeAtual = rentabilidadeAtual; }
}
