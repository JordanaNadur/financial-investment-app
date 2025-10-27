package com.financial.apigateway.domain.port.outbound;

import com.financial.apigateway.domain.model.JwtToken;
import reactor.core.publisher.Mono;
public interface TokenValidationPort {

    Mono<JwtToken> validateJwtToken(String token);

    Mono<JwtToken> extractTokenInfo(String token);
}
