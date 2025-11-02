package com.financial.portfolio.infrastructure.controller;

import com.financial.portfolio.application.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Wallet", description = "Endpoints para gerenciamento de carteira do cliente")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{clientId}")
    @Operation(summary = "Obter saldo", description = "Retorna o saldo atual da carteira do cliente")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(@PathVariable Long clientId) {
        return ResponseEntity.ok(Map.of("balance", walletService.getBalance(clientId)));
    }

    @PostMapping("/{clientId}/credit")
    @Operation(summary = "Creditar saldo", description = "Credita um valor na carteira do cliente")
    public ResponseEntity<Void> credit(@PathVariable Long clientId, @RequestParam BigDecimal amount) {
        walletService.credit(clientId, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{clientId}/debit")
    @Operation(summary = "Debitar saldo", description = "Debita um valor da carteira do cliente se houver saldo suficiente")
    public ResponseEntity<Map<String, Object>> debit(@PathVariable Long clientId, @RequestParam BigDecimal amount) {
        boolean ok = walletService.debit(clientId, amount);
        if (!ok) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Saldo insuficiente"));
        }
        return ResponseEntity.ok(Map.of("success", true));
    }
}
