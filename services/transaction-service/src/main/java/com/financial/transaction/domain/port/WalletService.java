package com.financial.transaction.domain.port;

import java.math.BigDecimal;

public interface WalletService {
    boolean hasSufficientBalance(Long clientId, BigDecimal amount);
    void debitBalance(Long clientId, BigDecimal amount);
}
