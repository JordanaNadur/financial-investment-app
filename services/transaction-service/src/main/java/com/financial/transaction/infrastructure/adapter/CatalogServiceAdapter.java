package com.financial.transaction.infrastructure.adapter;

import com.financial.transaction.domain.port.CatalogService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CatalogServiceAdapter implements CatalogService {

    // Stub implementation - in real scenario, this would call Catalog Service via REST or messaging
    @Override
    public boolean isOptionValid(Long optionId, BigDecimal amount) {
        // Simulate validation: optionId must be > 0 and amount > 0
        return optionId > 0 && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
