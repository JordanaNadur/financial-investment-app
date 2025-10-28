package com.financial.portfolio.domain.port;

import com.financial.portfolio.domain.Portfolio;

import java.util.Optional;

public interface PortfolioRepository {
    Optional<Portfolio> findByName(String username);
    Optional<Portfolio> findById(Long id);
    Portfolio save(Portfolio portfolio);
    void deleteById(Long id);
    boolean existsById(Long id);
}
