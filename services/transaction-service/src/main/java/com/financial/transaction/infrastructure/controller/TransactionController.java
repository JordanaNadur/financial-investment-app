package com.financial.transaction.infrastructure.controller;

import com.financial.transaction.application.dto.TransactionRequest;
import com.financial.transaction.application.dto.TransactionResponse;
import com.financial.transaction.application.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/invest")
    public ResponseEntity<TransactionResponse> invest(@RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.processTransaction(request);
        return ResponseEntity.ok(response);
    }
}
