package com.financial.portfolio.infrastructure.adapter;

import com.financial.portfolio.domain.Portfolio;
import com.financial.portfolio.domain.port.PortfolioRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepositoryAdapter extends JpaRepository<Portfolio, Long>, PortfolioRepository {

    @Override
    Optional<Portfolio> findByName(String username);

//    @Override
//    Optional<Portfolio> findByEmail(String email);
}
