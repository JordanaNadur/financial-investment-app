package com.financial.catalog.domain.usecase;

import java.util.Optional;

import com.financial.catalog.domain.*;
import com.financial.catalog.domain.port.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductManagementUseCase {

    private final ProductRepository productRepository;

    public Product createProduct(Product product){
        
        // Verifica se tem produto com mesmo nome
        Optional<Product> existing = productRepository.findByName(product.getName());
        if (existing.isPresent()){
            throw new IllegalArgumentException("Já existe um produto com esse nome: " + product.getName());
        }

        return productRepository.save(product);
    }

    public boolean deleteProduct(Long id){
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
