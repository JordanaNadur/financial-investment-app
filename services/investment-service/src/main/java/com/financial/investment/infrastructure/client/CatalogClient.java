package com.financial.investment.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CatalogClient {
    private static final Logger log = LoggerFactory.getLogger(CatalogClient.class);

    @Value("${catalog.service.base-url:http://catalog-service:8084/api}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ProductSummary getProduct(Long id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
            String token = resolveBearerToken();
            if (token != null) {
                headers.setBearerAuth(token);
            }
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<ProductSummary> resp = restTemplate.exchange(
                baseUrl + "/products/" + id,
                HttpMethod.GET,
                entity,
                ProductSummary.class
            );
            return resp.getBody();
        } catch (Exception e) {
            log.warn("Falha ao buscar produto {} no catalog-service: {}", id, e.getMessage());
            return null;
        }
    }

    private String resolveBearerToken() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof JwtAuthenticationToken jwtAuth) {
                Jwt jwt = jwtAuth.getToken();
                return jwt.getTokenValue();
            }
        } catch (Exception ignored) { }
        return null;
    }

    public record ProductSummary(Long id, String name, String type) { }
}
