package com.financial.investment.infrastructure.controller;

import com.financial.investment.application.dto.InvestmentRequest;
import com.financial.investment.application.dto.InvestmentResponse;
import com.financial.investment.application.service.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController
@RequestMapping("/investments")
@Tag(name = "Investments", description = "API para gerenciamento de investimentos")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Criar investimento")
    public ResponseEntity<InvestmentResponse> createInvestment(@Valid @RequestBody InvestmentRequest request, Authentication auth) {
        Long userId = getUserIdFromAuth(auth);
        InvestmentResponse response = investmentService.createInvestment(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Listar investimentos do usuário")
    public ResponseEntity<Page<InvestmentResponse>> getInvestments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        Long userId = getUserIdFromAuth(auth);
        Pageable pageable = PageRequest.of(page, size);
        Page<InvestmentResponse> investments = investmentService.getInvestments(userId, pageable);
        return ResponseEntity.ok(investments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Obter investimento por ID")
    public ResponseEntity<InvestmentResponse> getInvestment(@PathVariable Long id, Authentication auth) {
        Long userId = getUserIdFromAuth(auth);
        Optional<InvestmentResponse> response = investmentService.getInvestment(userId, id);
        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Resgatar investimento")
    public ResponseEntity<BigDecimal> withdrawInvestment(@PathVariable Long id, Authentication auth) {
        Long userId = getUserIdFromAuth(auth);
        BigDecimal amount = investmentService.withdrawInvestment(userId, id);
        return ResponseEntity.ok(amount);
    }

    @GetMapping("/report/summary")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Resumo agregado por tipo de produto")
    public ResponseEntity<Map<String, BigDecimal>> getSummary(Authentication auth) {
    Long userId = getUserIdFromAuth(auth);
    List<InvestmentResponse> list = investmentService.getInvestments(userId);
    Map<String, BigDecimal> summary = list.stream()
        .collect(Collectors.groupingBy(
            inv -> inv.getProductType() != null ? inv.getProductType() : (inv.getModalidade() != null ? inv.getModalidade() : "N/A"),
            Collectors.mapping(InvestmentResponse::getValor,
                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
        ));
    return ResponseEntity.ok(summary);
    }

    private Long getUserIdFromAuth(Authentication auth) {
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            Object claim = jwt.getClaims().get("userId");
            if (claim instanceof Number num) {
                return num.longValue();
            }
            if (claim instanceof String s) {
                try { return Long.parseLong(s); } catch (NumberFormatException ignored) {}
            }
        }
        throw new IllegalStateException("userId não encontrado no token JWT");
    }
}
