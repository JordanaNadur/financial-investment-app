package com.financial.transaction.infrastructure.adapter;

import com.financial.transaction.domain.port.WalletService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class WalletServiceAdapter implements WalletService {

    // Stub implementation - in real scenario, this would call Wallet Service via REST or messaging
    private final Map<Long, BigDecimal> balances = new HashMap<>();

    public WalletServiceAdapter() {
        // Initialize some test balances
        balances.put(1L, new BigDecimal("10000.00"));
        balances.put(2L, new BigDecimal("5000.00"));
    }

    @Override
    public boolean hasSufficientBalance(Long clientId, BigDecimal amount) {
        BigDecimal balance = balances.getOrDefault(clientId, BigDecimal.ZERO);
        return balance.compareTo(amount) >= 0;
    }

    @Override
    public void debitBalance(Long clientId, BigDecimal amount) {
        BigDecimal current = balances.getOrDefault(clientId, BigDecimal.ZERO);
        balances.put(clientId, current.subtract(amount));
    }
}
