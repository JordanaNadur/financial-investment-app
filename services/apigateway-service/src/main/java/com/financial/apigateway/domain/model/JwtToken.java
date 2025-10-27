package com.financial.apigateway.domain.model;

import java.time.LocalDateTime;
import java.util.Map;

public class JwtToken {
    private final String token;
    private final String subject;
    private final LocalDateTime expirationTime;
    private final Map<String, Object> claims;

    public JwtToken(String token, String subject, LocalDateTime expirationTime, Map<String, Object> claims) {
        this.token = token;
        this.subject = subject;
        this.expirationTime = expirationTime;
        this.claims = claims;
    }

    public String getToken() {
        return token;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }
}
