package com.dan.franchiseapi.product.service;

import com.dan.franchiseapi.branch.service.BranchService;
import com.dan.franchiseapi.common.exception.ConflictException;
import com.dan.franchiseapi.common.exception.NotFoundException;
import com.dan.franchiseapi.product.dto.CreateProductRequest;
import com.dan.franchiseapi.product.dto.ProductResponse;
import com.dan.franchiseapi.product.dto.UpdateProductNameRequest;
import com.dan.franchiseapi.product.dto.UpdateProductStockRequest;
import com.dan.franchiseapi.product.mapper.ProductMapper;
import com.dan.franchiseapi.product.model.Product;
import com.dan.franchiseapi.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BranchService branchService;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            BranchService branchService
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.branchService = branchService;
    }

    public Mono<ProductResponse> createProduct(Long branchId, CreateProductRequest request) {
        return branchService.getBranchById(branchId)
                .then(productRepository.existsByBranchIdAndName(branchId, request.name()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ConflictException("Product name already exists in this branch"));
                    }

                    Product product = productMapper.toEntity(branchId, request);
                    return productRepository.save(product)
                            .map(productMapper::toResponse);
                });
    }

    public Mono<Void> deleteProduct(Long branchId, Long productId) {
        return branchService.getBranchById(branchId)
                .then(productRepository.findByIdAndBranchId(productId, branchId))
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found in the given branch")))
                .flatMap(productRepository::delete);
    }

    public Mono<ProductResponse> updateProductStock(Long productId, UpdateProductStockRequest request) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found")))
                .flatMap(existingProduct -> {
                    existingProduct.setStock(request.stock());
                    return productRepository.save(existingProduct);
                })
                .map(productMapper::toResponse);
    }

    public Mono<ProductResponse> updateProductName(Long productId, UpdateProductNameRequest request) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found")))
                .flatMap(existingProduct ->
                        productRepository.existsByBranchIdAndName(existingProduct.getBranchId(), request.name())
                                .flatMap(exists -> {
                                    if (exists && !existingProduct.getName().equals(request.name())) {
                                        return Mono.error(new ConflictException("Product name already exists in this branch"));
                                    }

                                    existingProduct.setName(request.name());
                                    return productRepository.save(existingProduct);
                                })
                )
                .map(productMapper::toResponse);
    }

    public Mono<Product> getProductById(Long productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found")));
    }

}
