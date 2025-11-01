package com.financial.transaction.application.service;

import com.financial.transaction.application.dto.TransactionRequest;
import com.financial.transaction.application.dto.TransactionResponse;
import com.financial.transaction.domain.Transaction;
import com.financial.transaction.domain.usecase.ProcessTransactionUseCase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final ProcessTransactionUseCase processTransactionUseCase;

    public TransactionService(ProcessTransactionUseCase processTransactionUseCase) {
        this.processTransactionUseCase = processTransactionUseCase;
    }

    public TransactionResponse processTransaction(TransactionRequest request) {
        Transaction.TransactionType type = Transaction.TransactionType.valueOf(request.getType().toUpperCase());
        Transaction transaction = processTransactionUseCase.execute(request.getClientId(), request.getOptionId(), request.getAmount(), type);
        return new TransactionResponse(transaction);
    }
}
