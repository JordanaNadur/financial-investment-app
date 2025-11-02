package com.financial.transaction.infrastructure.adapter;

import com.financial.transaction.domain.port.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class WalletServiceAdapter implements WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletServiceAdapter.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${portfolio.service.base-url:http://portfolio-service:8085}")
    private String portfolioServiceBaseUrl;

    @Override
    public boolean hasSufficientBalance(Long clientId, BigDecimal amount) {
        try {
            String url = portfolioServiceBaseUrl + "/api/wallet/" + clientId;
            ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                Object balanceObj = resp.getBody().get("balance");
                BigDecimal balance = new BigDecimal(String.valueOf(balanceObj));
                return balance.compareTo(amount) >= 0;
            } else {
                log.error("Consulta de saldo retornou status {}", resp.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Erro ao consultar saldo da carteira: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public void debitBalance(Long clientId, BigDecimal amount) {
        String url = portfolioServiceBaseUrl + "/api/wallet/" + clientId + "/debit?amount=" + amount;
        ResponseEntity<Map> resp = restTemplate.postForEntity(url, null, Map.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            log.error("Débito de wallet falhou: status={}", resp.getStatusCode());
            throw new IllegalStateException("Falha ao debitar carteira do cliente " + clientId);
        }
    }

    @Override
    public void creditBalance(Long clientId, BigDecimal amount) {
        String url = portfolioServiceBaseUrl + "/api/wallet/" + clientId + "/credit?amount=" + amount;
        ResponseEntity<Void> resp = restTemplate.postForEntity(url, null, Void.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            log.error("Crédito de wallet falhou: status={}", resp.getStatusCode());
            throw new IllegalStateException("Falha ao creditar carteira do cliente " + clientId);
        }
    }
}
