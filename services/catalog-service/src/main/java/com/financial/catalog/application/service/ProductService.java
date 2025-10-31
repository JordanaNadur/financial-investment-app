package com.financial.catalog.application.service;

import com.financial.catalog.application.dto.ProductRequest;
import com.financial.catalog.application.dto.ProductResponse;
import com.financial.catalog.domain.Product;
import com.financial.catalog.domain.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductManagementUseCase productManagementUseCase;
    private final GetProductUseCase getProductUseCase;

    public ProductResponse createProduct(ProductRequest request){
        Product product = new Product();
            product.setName(request.getName());
            product.setType(request.getType());
            product.setDescription(request.getDescription());
            product.setRiskLevel(request.getRiskLevel());
            product.setMinimumInvestment(request.getMinimumInvestment());
            product.setMonthlyReturn(request.getMonthlyReturn());
            product.setMinimumTermMonths(request.getMinimumTermMonths());
            product.setActive(request.getActive());

            Product saved = productManagementUseCase.createProduct(product);
            return toResponse(saved);
    }

    public ProductResponse getProductById(Long id){
        Product product = getProductUseCase.getById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return toResponse(product);
    }

    public ProductResponse getProductByName(String name){
        Product product = getProductUseCase.getByName(name)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return toResponse(product);
    }

    public List<ProductResponse> listAllProducts(){
        return getProductUseCase.listAll()
        .stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
    }

    public void deleteById(Long id){
        boolean deleted = productManagementUseCase.deleteProduct(id);
        if(!deleted){
            throw new RuntimeException("Produto não encontrado");
        }
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .type(product.getType())
                .description(product.getDescription())
                .riskLevel(product.getRiskLevel())
                .minimumInvestment(product.getMinimumInvestment())
                .monthlyReturn(product.getMonthlyReturn())
                .minimumTermMonths(product.getMinimumTermMonths())
                .active(product.getActive())
                .build();
    }

}
