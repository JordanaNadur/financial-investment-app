package com.financial.investment.infrastructure.client;

import com.financial.investment.application.dto.TransactionWithdrawRequest;
import com.financial.investment.application.dto.TransactionInvestRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TransactionClient {

    private static final Logger log = LoggerFactory.getLogger(TransactionClient.class);

    @Value("${transaction.service.base-url:http://transaction-service:8083/api}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void registerWithdraw(TransactionWithdrawRequest request) {
        String url = baseUrl + "/transactions/withdraw";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionWithdrawRequest> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[transaction-service] withdraw falhou: status={} body={} ", response.getStatusCode(), response.getBody());
                throw new IllegalStateException("Falha ao registrar withdraw no transaction-service");
            }
            log.info("[transaction-service] withdraw status: {}", response.getStatusCode());
        } catch (Exception ex) {
            log.error("Falha ao chamar transaction-service para withdraw: {}", ex.getMessage());
            throw ex;
        }
    }

    public void registerInvest(TransactionInvestRequest request) {
        String url = baseUrl + "/transactions/invest";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionInvestRequest> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[transaction-service] invest falhou: status={} body={}", response.getStatusCode(), response.getBody());
                throw new IllegalStateException("Falha ao registrar invest no transaction-service");
            }
            log.info("[transaction-service] invest status: {}", response.getStatusCode());
        } catch (Exception ex) {
            log.error("Falha ao chamar transaction-service para invest: {}", ex.getMessage());
            throw ex;
        }
    }
}
