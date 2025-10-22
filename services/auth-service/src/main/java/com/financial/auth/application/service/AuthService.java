package com.financial.auth.application.service;

import com.financial.auth.application.dto.AuthRequest;
import com.financial.auth.application.dto.AuthResponse;
import com.financial.auth.application.dto.RegisterRequest;
import com.financial.auth.domain.User;
import com.financial.auth.domain.usecase.AuthenticateUserUseCase;
import com.financial.auth.domain.usecase.RegisterUserUseCase;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshExpiration;

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public AuthService(AuthenticateUserUseCase authenticateUserUseCase, RegisterUserUseCase registerUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.registerUserUseCase = registerUserUseCase;
    }

    public AuthResponse authenticate(AuthRequest request) {
        Optional<User> userOpt = authenticateUserUseCase.execute(request.getUsername(), request.getPassword());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = generateToken(user);
            String refreshToken = generateRefreshToken(user);
            return new AuthResponse(token, refreshToken, user.getUsername(), user.getRole().name());
        }
        throw new RuntimeException("Invalid credentials");
    }

    public AuthResponse register(RegisterRequest request) {
        User user = registerUserUseCase.execute(request.getUsername(), request.getEmail(), request.getPassword(), request.getRole());
        String token = generateToken(user);
        String refreshToken = generateRefreshToken(user);
        return new AuthResponse(token, refreshToken, user.getUsername(), user.getRole().name());
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    private String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
                .signWith(key)
                .compact();
    }
}
