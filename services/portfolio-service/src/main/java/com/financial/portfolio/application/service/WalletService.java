package com.financial.portfolio.application.service;

import com.financial.portfolio.infrastructure.persistence.entity.WalletEntity;
import com.financial.portfolio.infrastructure.persistence.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long clientId) {
        return walletRepository.findById(clientId)
                .map(WalletEntity::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional
    public void credit(Long clientId, BigDecimal amount) {
        WalletEntity wallet = walletRepository.findById(clientId)
                .orElse(WalletEntity.builder().clientId(clientId).balance(BigDecimal.ZERO).build());
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    @Transactional
    public boolean debit(Long clientId, BigDecimal amount) {
        WalletEntity wallet = walletRepository.findById(clientId)
                .orElse(WalletEntity.builder().clientId(clientId).balance(BigDecimal.ZERO).build());
        if (wallet.getBalance().compareTo(amount) < 0) return false;
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        return true;
    }
}
