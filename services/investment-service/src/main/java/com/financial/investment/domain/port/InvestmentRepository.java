package com.financial.investment.domain.port;

import com.financial.investment.domain.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InvestmentRepository {
    Optional<Investment> findById(Long id);
    Page<Investment> findByUserId(Long userId, Pageable pageable);
    Investment save(Investment investment);
    Optional<Investment> findByIdAndUserId(Long id, Long userId);
}
