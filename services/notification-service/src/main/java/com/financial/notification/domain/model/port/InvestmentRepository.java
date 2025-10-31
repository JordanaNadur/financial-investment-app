package com.financial.notification.domain.model.port;

import com.financial.notification.domain.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    // Apenas métodos básicos que sabemos que funcionam
    List<Investment> findByUserId(Long userId);
    Optional<Investment> findByIdAndUserId(Long id, Long userId);
    List<Investment> findByInvestmentId(Long investmentId);
    List<Investment> findByStatus(Investment.InvestmentStatus status);
    List<Investment> findByType(Investment.InvestmentType type);

    // Métodos simples sem queries complexas
    List<Investment> findByUserIdAndStatus(Long userId, Investment.InvestmentStatus status);
    Long countByUserIdAndStatus(Long userId, Investment.InvestmentStatus status);

    // Buscar por usuário e tipo
    List<Investment> findByUserIdAndType(Long userId, Investment.InvestmentType type);
}