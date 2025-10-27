package com.financial.portfolio.domain.port;

import com.financial.portfolio.domain.Portfolio;

import java.util.Optional;

public interface PortfolioRepository {
    Optional<Portfolio> findByName(String username);

}
