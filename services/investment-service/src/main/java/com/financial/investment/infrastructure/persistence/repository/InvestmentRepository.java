package com.financial.investment.infrastructure.persistence.repository;

import com.financial.investment.infrastructure.persistence.entity.InvestmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentEntity, Long> {

    Page<InvestmentEntity> findByUserId(Long userId, Pageable pageable);

    List<InvestmentEntity> findByUserId(Long userId);

    Optional<InvestmentEntity> findByIdAndUserId(Long id, Long userId);
}