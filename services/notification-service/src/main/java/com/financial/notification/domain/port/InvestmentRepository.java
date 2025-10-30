package com.financial.notification.domain.port;

import com.financial.notification.domain.model.Investment;
import java.util.List;
import java.util.Optional;

public interface InvestmentRepository {
    Optional<Investment> findById(Long id);
    List<Investment> findAll();
    Investment save(Investment investment);
    List<Investment> findByUserId(Long userId);
    List<Investment> findByStatus(Investment.InvestmentStatus status);
    Optional<Investment> findByInvestmentId(Long investmentId);
}