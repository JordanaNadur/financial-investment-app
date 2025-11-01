package com.financial.transaction.infrastructure.adapter;

import com.financial.transaction.domain.port.PortfolioService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PortfolioServiceAdapter implements PortfolioService {

    // Stub implementation - in real scenario, this would call Portfolio Service via REST or messaging
    @Override
    public void addApplication(Long clientId, Long optionId, BigDecimal amount) {
        // Simulate adding to portfolio
        System.out.println("Added application: Client " + clientId + ", Option " + optionId + ", Amount " + amount);
    }
}
