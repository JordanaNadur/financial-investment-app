package com.financial.transaction.infrastructure.controller;

import com.financial.transaction.application.dto.TransactionRequest;
import com.financial.transaction.application.dto.TransactionResponse;
import com.financial.transaction.application.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/invest")
    public ResponseEntity<TransactionResponse> invest(@RequestBody TransactionRequest request) {
    request.setType("APORTE");
        TransactionResponse response = transactionService.processTransaction(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest request) {
        request.setType("RESGATE");
        TransactionResponse response = transactionService.processTransaction(request);
        return ResponseEntity.ok(response);
    }
}
