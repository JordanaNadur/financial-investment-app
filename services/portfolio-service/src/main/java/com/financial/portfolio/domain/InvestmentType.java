package com.financial.portfolio.domain;

public enum InvestmentType {
    ACAO("Ação"),
    FUNDO("Fundo de Investimento"),
    TESOURO("Tesouro Direto"),
    CRIPTO("Criptomoeda"),
    RENDA_FIXA("Renda Fixa"),
    CDB("Certificado de Depósito Bancário"),
    LCI("Letra de Crédito Imobiliário"),
    LCA("Letra de Crédito do Agronegócio"),
    DEBENTURE("Debênture"),
    FII("Fundo Imobiliário"),
    ETF("Exchange Traded Fund"),
    OURO("Ouro"),
    CAMBIO("Câmbio");

    private final String description;

    InvestmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

