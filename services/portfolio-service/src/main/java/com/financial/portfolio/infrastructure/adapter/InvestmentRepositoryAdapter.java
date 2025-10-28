package com.financial.portfolio.infrastructure.adapter;

import com.financial.portfolio.domain.Investment;
import com.financial.portfolio.domain.port.InvestmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentRepositoryAdapter extends JpaRepository<Investment, Long>, InvestmentRepository {

    @Override
    Optional<Investment> findByName(String name);

}

