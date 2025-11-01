package com.financial.auth.infrastructure.controller;

import com.financial.auth.application.dto.AuthRequest;
import com.financial.auth.application.dto.AuthResponse;
import com.financial.auth.application.dto.RegisterRequest;
import com.financial.auth.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    @SecurityRequirements
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register user")
    @SecurityRequirements
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/welcome")
    @Operation(summary = "Welcome endpoint")
    @SecurityRequirements
    public ResponseEntity<Map<String, String>> welcome(HttpServletRequest request) {
        logger.info("Request received: {} {}", request.getMethod(), request.getRequestURI());
        Map<String, String> response = Map.of("message", "Welcome to the Financial Investment App!");
        return ResponseEntity.ok(response);
    }
}
