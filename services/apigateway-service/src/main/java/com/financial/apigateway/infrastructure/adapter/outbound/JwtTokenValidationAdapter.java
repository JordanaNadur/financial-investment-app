package com.financial.apigateway.infrastructure.adapter.outbound;

import com.financial.apigateway.domain.model.JwtToken;
import com.financial.apigateway.domain.port.outbound.TokenValidationPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtTokenValidationAdapter implements TokenValidationPort {

    private final SecretKey secretKey;

    public JwtTokenValidationAdapter(@Value("${jwt.secret:mySecretKey123456789012345678901234567890}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public Mono<JwtToken> validateJwtToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String subject = claims.getSubject();
                Date expiration = claims.getExpiration();
                LocalDateTime expirationTime = expiration.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                Map<String, Object> claimsMap = new HashMap<>(claims);

                return new JwtToken(token, subject, expirationTime, claimsMap);
            } catch (Exception e) {
                throw new RuntimeException("Invalid token", e);
            }
        });
    }

    @Override
    public Mono<JwtToken> extractTokenInfo(String token) {
        return validateJwtToken(token);
    }
}
