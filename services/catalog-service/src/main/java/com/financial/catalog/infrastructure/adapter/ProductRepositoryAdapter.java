package com.financial.catalog.infrastructure.adapter;

import com.financial.catalog.domain.Product;
import com.financial.catalog.port.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepositoryAdapter extends JpaRepository<Product, Long>, ProductRepository {

    @Override
    Optional<Product> findByName(String name);

}