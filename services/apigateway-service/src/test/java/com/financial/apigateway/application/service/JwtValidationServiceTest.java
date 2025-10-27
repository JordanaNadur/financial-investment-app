package com.financial.apigateway.application.service;

import com.financial.apigateway.domain.model.JwtToken;
import com.financial.apigateway.domain.port.outbound.TokenValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

class JwtValidationServiceTest {

    @Mock
    private TokenValidationPort tokenValidationPort;

    private JwtValidationService jwtValidationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtValidationService = new JwtValidationService(tokenValidationPort);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String token = "valid-token";
        Map<String, Object> claims = new HashMap<>();
        JwtToken jwtToken = new JwtToken(token, "user123", LocalDateTime.now().plusHours(1), claims);
        
        when(tokenValidationPort.validateJwtToken(token)).thenReturn(Mono.just(jwtToken));

        StepVerifier.create(jwtValidationService.validateToken(token))
                .expectNext(jwtToken)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorForNullToken() {
        StepVerifier.create(jwtValidationService.validateToken(null))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorForEmptyToken() {
        StepVerifier.create(jwtValidationService.validateToken(""))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void shouldDetectExpiredToken() {
        String token = "expired-token";
        Map<String, Object> claims = new HashMap<>();
        JwtToken expiredToken = new JwtToken(token, "user123", LocalDateTime.now().minusHours(1), claims);
        
        when(tokenValidationPort.extractTokenInfo(token)).thenReturn(Mono.just(expiredToken));

        StepVerifier.create(jwtValidationService.isTokenExpired(token))
                .expectNext(true)
                .verifyComplete();
    }
}
