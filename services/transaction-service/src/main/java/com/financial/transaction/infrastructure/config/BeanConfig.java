package com.financial.transaction.infrastructure.config;

import com.financial.transaction.domain.port.CatalogService;
import com.financial.transaction.domain.port.PortfolioService;
import com.financial.transaction.domain.port.WalletService;
import com.financial.transaction.infrastructure.adapter.CatalogServiceAdapter;
import com.financial.transaction.infrastructure.adapter.PortfolioServiceAdapter;
import com.financial.transaction.infrastructure.adapter.WalletServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CatalogService catalogService() {
        return new CatalogServiceAdapter();
    }

    @Bean
    public WalletService walletService() {
        return new WalletServiceAdapter();
    }

    @Bean
    public PortfolioService portfolioService() {
        return new PortfolioServiceAdapter();
    }
}
