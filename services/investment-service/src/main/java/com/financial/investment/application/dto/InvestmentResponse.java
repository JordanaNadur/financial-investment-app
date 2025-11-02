package com.financial.investment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentResponse {
    private Long id;
    private BigDecimal valor;
    private BigDecimal retornoSimulado;
    private String modalidade;
    private String productName;
    private String productType;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataResgate;
    private Boolean ativo;

    // Construtor para compatibilidade
    public InvestmentResponse(Long id, BigDecimal valor, BigDecimal retornoSimulado, String modalidade) {
        this.id = id;
        this.valor = valor;
        this.retornoSimulado = retornoSimulado;
        this.modalidade = modalidade;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }
}