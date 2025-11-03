package com.financial.investment.application.dto;

import java.math.BigDecimal;

public record TransactionInvestRequest(
        Long clientId,
        Long optionId,
        BigDecimal amount
) {}
