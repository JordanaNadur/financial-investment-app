package com.financial.transaction.domain.port;

import java.math.BigDecimal;

public interface PortfolioService {
    void addApplication(Long clientId, Long optionId, BigDecimal amount);
    void removeApplication(Long clientId, Long optionId, BigDecimal amount);
}
