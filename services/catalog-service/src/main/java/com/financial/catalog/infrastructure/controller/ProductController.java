package com.financial.catalog.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financial.catalog.application.dto.ProductRequest;
import com.financial.catalog.application.dto.ProductResponse;
import com.financial.catalog.application.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API para gerenciamento de produtos do catálogo")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Criar um novo produto no catálogo")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter produto por ID")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Obter produto por nome")
    public ResponseEntity<ProductResponse> getByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos do catálogo")
    public ResponseEntity<List<ProductResponse>> listAll() {
        return ResponseEntity.ok(productService.listAllProducts());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um produto do catálogo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}