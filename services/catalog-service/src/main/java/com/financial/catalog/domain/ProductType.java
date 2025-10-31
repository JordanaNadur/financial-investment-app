package com.financial.catalog.domain;

public enum ProductType {
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

    ProductType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

