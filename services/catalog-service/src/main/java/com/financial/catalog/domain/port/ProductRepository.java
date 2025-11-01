package com.financial.catalog.domain.port;

import java.util.List;
import java.util.Optional;

import com.financial.catalog.domain.Product;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    List<Product> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}