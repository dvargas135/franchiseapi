package com.dan.franchiseapi.product.mapper;

import com.dan.franchiseapi.product.dto.CreateProductRequest;
import com.dan.franchiseapi.product.dto.ProductResponse;
import com.dan.franchiseapi.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(Long branchId, CreateProductRequest request) {
        return new Product(branchId, request.name(), request.stock());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getBranchId(),
                product.getName(),
                product.getStock()
        );
    }

}
