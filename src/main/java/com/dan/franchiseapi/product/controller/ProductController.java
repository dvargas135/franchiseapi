package com.dan.franchiseapi.product.controller;

import com.dan.franchiseapi.product.dto.CreateProductRequest;
import com.dan.franchiseapi.product.dto.ProductResponse;
import com.dan.franchiseapi.product.dto.UpdateProductNameRequest;
import com.dan.franchiseapi.product.dto.UpdateProductStockRequest;
import com.dan.franchiseapi.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> createProduct(
            @PathVariable Long branchId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.createProduct(branchId, request);
    }

    @DeleteMapping("/api/branches/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(
            @PathVariable Long branchId,
            @PathVariable Long productId
    ) {
        return productService.deleteProduct(branchId, productId);
    }

    @PatchMapping("/api/products/{productId}/stock")
    public Mono<ProductResponse> updateProductStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequest request
    ) {
        return productService.updateProductStock(productId, request);
    }

    @PatchMapping("/api/products/{productId}/name")
    public Mono<ProductResponse> updateProductName(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductNameRequest request
    ) {
        return productService.updateProductName(productId, request);
    }

}
