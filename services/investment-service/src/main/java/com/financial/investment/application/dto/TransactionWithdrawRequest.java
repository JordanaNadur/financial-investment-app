package com.financial.investment.application.dto;

import java.math.BigDecimal;

public record TransactionWithdrawRequest(
        Long clientId,
        Long optionId,
        BigDecimal amount
) {}
