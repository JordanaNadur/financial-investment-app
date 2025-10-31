package com.financial.catalog.domain.usecase;

import java.util.List;
import java.util.Optional;

import com.financial.catalog.domain.Product;
import com.financial.catalog.domain.port.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetProductUseCase {

    private final ProductRepository productRepository;

    public Optional<Product> getById (Long id){
        return productRepository.findById(id);
    }

    public Optional<Product> getByName (String name){
        return productRepository.findByName(name);
    }

    public List<Product> listAll (){
        return productRepository.findAll();
    }
    
}
