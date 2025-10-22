package com.financial.investment.infrastructure.adapter;

import com.financial.investment.domain.Investment;
import com.financial.investment.domain.port.InvestmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentRepositoryAdapter extends JpaRepository<Investment, Long>, InvestmentRepository {

    @Override
    Page<Investment> findByUserId(Long userId, Pageable pageable);

    @Override
    @Query("SELECT i FROM Investment i WHERE i.id = :id AND i.userId = :userId")
    Optional<Investment> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
