package com.financial.portfolio.infrastructure.controller;

import com.financial.portfolio.application.dto.PortfolioRegisterRequest;
import com.financial.portfolio.application.dto.PortfolioResponse;
import com.financial.portfolio.application.dto.PortfolioUpdateRequest;
import com.financial.portfolio.application.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
@Tag(name = "Portfolio", description = "Endpoints para gerenciamento de portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    @Operation(summary = "Buscar portfolio por nome", description = "Retorna um portfolio baseado no nome fornecido")
    public ResponseEntity<PortfolioResponse> getByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(portfolioService.getPortfolioByName(name));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo portfolio", description = "Cria um novo portfolio para um cliente")
    public ResponseEntity<PortfolioResponse> register(@Valid @RequestBody PortfolioRegisterRequest request) {
        PortfolioResponse response = portfolioService.registerPortfolio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar portfolio", description = "Atualiza as informações de um portfolio existente")
    public ResponseEntity<PortfolioResponse> update(
            @PathVariable Long id, 
            @Valid @RequestBody PortfolioUpdateRequest request) throws Exception {
        PortfolioResponse response = portfolioService.updatePortfolio(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover portfolio", description = "Remove um portfolio pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }

}
