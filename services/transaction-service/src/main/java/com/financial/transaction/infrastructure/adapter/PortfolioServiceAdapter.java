package com.financial.transaction.infrastructure.adapter;

import com.financial.transaction.domain.port.PortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class PortfolioServiceAdapter implements PortfolioService {

    private static final Logger log = LoggerFactory.getLogger(PortfolioServiceAdapter.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${portfolio.service.base-url:http://portfolio-service:8085}")
    private String portfolioServiceBaseUrl;

    @Override
    public void addApplication(Long clientId, Long optionId, BigDecimal amount) {
        try {
            String url = portfolioServiceBaseUrl + "/api/investment";
            Map<String, Object> body = Map.of(
                    "name", "option-" + optionId,
                    "type", "FUNDO",
                    "amount", amount,
                    "unitPrice", BigDecimal.ONE,
                    "portfolioId", clientId // simplificação: 1 carteira por cliente
            );
            ResponseEntity<Map> resp = restTemplate.postForEntity(url, body, Map.class);
            if (!resp.getStatusCode().is2xxSuccessful()) {
                log.warn("Falha ao adicionar aplicação no portfolio: status {}", resp.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Erro ao adicionar aplicação no portfolio: {}", e.getMessage());
        }
    }

    @Override
    public void removeApplication(Long clientId, Long optionId, BigDecimal amount) {
        try {
            // Nesta versão simplificada, apenas registramos uma remoção lógica (sem ID do investimento)
            String url = portfolioServiceBaseUrl + "/api/investment/name?name=option-" + optionId;
            ResponseEntity<Map> getResp = restTemplate.getForEntity(url, Map.class);
            if (getResp.getStatusCode().is2xxSuccessful() && getResp.getBody() != null) {
                Object idObj = getResp.getBody().get("id");
                if (idObj != null) {
                    Long id = Long.valueOf(String.valueOf(idObj));
                    String deleteUrl = portfolioServiceBaseUrl + "/api/investment/" + id;
                    restTemplate.delete(deleteUrl);
                }
            }
        } catch (Exception e) {
            log.error("Erro ao remover aplicação do portfolio: {}", e.getMessage());
        }
    }
}
