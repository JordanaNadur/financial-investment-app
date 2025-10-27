package com.financial.apigateway.application.service;

import com.financial.apigateway.domain.model.JwtToken;
import com.financial.apigateway.domain.port.inbound.JwtValidationUseCase;
import com.financial.apigateway.domain.port.outbound.TokenValidationPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class JwtValidationService implements JwtValidationUseCase {

    private final TokenValidationPort tokenValidationPort;

    public JwtValidationService(TokenValidationPort tokenValidationPort) {
        this.tokenValidationPort = tokenValidationPort;
    }

    @Override
    public Mono<JwtToken> validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Token cannot be null or empty"));
        }
        
        return tokenValidationPort.validateJwtToken(token)
                .onErrorMap(ex -> new RuntimeException("Token validation failed", ex));
    }

    @Override
    public Mono<Boolean> isTokenExpired(String token) {
        return tokenValidationPort.extractTokenInfo(token)
                .map(JwtToken::isExpired)
                .onErrorReturn(true);
    }
}
