package com.financial.apigateway.domain.port.inbound;

import com.financial.apigateway.domain.model.JwtToken;
import reactor.core.publisher.Mono;
public interface JwtValidationUseCase {
    
    Mono<JwtToken> validateToken(String token);

    Mono<Boolean> isTokenExpired(String token);
}
