package com.financial.notification.infrastructure.persistence;

import com.financial.notification.domain.model.Investment;
import com.financial.notification.domain.port.InvestmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaInvestmentRepository extends JpaRepository<Investment, Long>, InvestmentRepository {

    @Override
    @Query("SELECT i FROM Investment i WHERE i.userId = :userId ORDER BY i.createdAt DESC")
    List<Investment> findByUserId(@Param("userId") Long userId);

    @Override
    List<Investment> findByStatus(Investment.InvestmentStatus status);

    @Override
    Optional<Investment> findByInvestmentId(Long investmentId);

    @Query("SELECT i FROM Investment i WHERE i.investmentId = :investmentId AND i.type = :type ORDER BY i.createdAt DESC")
    Optional<Investment> findLatestByInvestmentIdAndType(@Param("investmentId") Long investmentId,
                                                         @Param("type") Investment.InvestmentType type);
}