package com.financial.portfolio.domain.port;

import com.financial.portfolio.domain.Investment;

import java.util.List;
import java.util.Optional;

public interface InvestmentRepository {
    Optional<Investment> findByName(String name);
    Optional<Investment> findById(Long id);
    List<Investment> findAll();
    Investment save(Investment investment);
    void deleteById(Long id);
    boolean existsById(Long id);
}

