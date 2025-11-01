package com.financial.transaction.domain.port;

import java.math.BigDecimal;

public interface CatalogService {
    boolean isOptionValid(Long optionId, BigDecimal amount);
}
