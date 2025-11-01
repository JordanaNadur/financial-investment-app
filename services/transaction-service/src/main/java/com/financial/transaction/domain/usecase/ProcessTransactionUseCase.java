package com.financial.transaction.domain.usecase;

import com.financial.transaction.domain.Transaction;
import com.financial.transaction.domain.port.CatalogService;
import com.financial.transaction.domain.port.PortfolioService;
import com.financial.transaction.domain.port.TransactionRepository;
import com.financial.transaction.domain.port.WalletService;
import com.financial.transaction.messaging.InvestmentRealizedEvent;
import com.financial.transaction.messaging.TransactionEventProducer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProcessTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final CatalogService catalogService;
    private final WalletService walletService;
    private final PortfolioService portfolioService;
    private final TransactionEventProducer eventProducer;

    public ProcessTransactionUseCase(TransactionRepository transactionRepository,
                                     CatalogService catalogService,
                                     WalletService walletService,
                                     PortfolioService portfolioService,
                                     TransactionEventProducer eventProducer) {
        this.transactionRepository = transactionRepository;
        this.catalogService = catalogService;
        this.walletService = walletService;
        this.portfolioService = portfolioService;
        this.eventProducer = eventProducer;
    }

    public Transaction execute(Long clientId, Long optionId, BigDecimal amount, Transaction.TransactionType type) {
        // Verificar se a opção existe e o valor é válido
        if (!catalogService.isOptionValid(optionId, amount)) {
            throw new IllegalArgumentException("Opção inválida ou valor não permitido");
        }

        // Verificar saldo do cliente
        if (!walletService.hasSufficientBalance(clientId, amount)) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        // Debitar saldo
        walletService.debitBalance(clientId, amount);

        // Adicionar à carteira
        portfolioService.addApplication(clientId, optionId, amount);

        // Criar transação
        Transaction transaction = new Transaction(clientId, optionId, amount, type);
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Publicar evento
        InvestmentRealizedEvent event = new InvestmentRealizedEvent(clientId, savedTransaction.getId(), "Investimento realizado com sucesso");
        eventProducer.sendInvestmentRealizedEvent(event);

        return savedTransaction;
    }
}
