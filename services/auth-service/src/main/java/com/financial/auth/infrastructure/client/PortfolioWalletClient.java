package com.financial.auth.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class PortfolioWalletClient {

    private static final Logger log = LoggerFactory.getLogger(PortfolioWalletClient.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${portfolio.service.base-url:http://portfolio-service:8085}")
    private String baseUrl;

    public void credit(Long clientId, BigDecimal amount) {
        try {
            String url = baseUrl + "/api/wallet/" + clientId + "/credit?amount=" + amount;
            restTemplate.postForEntity(url, null, Void.class);
            log.info("Wallet creditado com {} para cliente {}", amount, clientId);
        } catch (Exception e) {
            log.error("Falha ao creditar carteira no portfolio-service: {}", e.getMessage());
        }
    }
}
