package com.financial.catalog.infrastructure.config;

import com.financial.catalog.domain.port.ProductRepository;
import com.financial.catalog.domain.usecase.GetProductUseCase;
import com.financial.catalog.domain.usecase.ProductManagementUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProductManagementUseCase productManagementUseCase(ProductRepository productRepository) {
        return new ProductManagementUseCase(productRepository);
    }

    @Bean
    public GetProductUseCase getProductUseCase(ProductRepository productRepository) {
        return new GetProductUseCase(productRepository);
    }

}
