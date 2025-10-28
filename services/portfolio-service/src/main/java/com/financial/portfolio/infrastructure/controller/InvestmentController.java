package com.financial.portfolio.infrastructure.controller;

import com.financial.portfolio.application.dto.InvestmentRegisterRequest;
import com.financial.portfolio.application.dto.InvestmentResponse;
import com.financial.portfolio.application.dto.InvestmentUpdateRequest;
import com.financial.portfolio.application.service.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/investment")
@Tag(name = "Investment", description = "Endpoints para gerenciamento de investimentos")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @GetMapping("/name")
    @Operation(summary = "Buscar investimento por nome", description = "Retorna um investimento baseado no nome fornecido")
    public ResponseEntity<InvestmentResponse> getByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(investmentService.getInvestmentByName(name));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar investimento por ID", description = "Retorna um investimento baseado no ID fornecido")
    public ResponseEntity<InvestmentResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(investmentService.getInvestmentById(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo investimento", description = "Cria um novo investimento em um portfolio")
    public ResponseEntity<InvestmentResponse> register(@Valid @RequestBody InvestmentRegisterRequest request) {
        InvestmentResponse response = investmentService.registerInvestment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar investimento", description = "Atualiza as informações de um investimento existente")
    public ResponseEntity<InvestmentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InvestmentUpdateRequest request) throws Exception {
        InvestmentResponse response = investmentService.updateInvestment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover investimento", description = "Remove um investimento pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        investmentService.deleteInvestment(id);
        return ResponseEntity.noContent().build();
    }

}

