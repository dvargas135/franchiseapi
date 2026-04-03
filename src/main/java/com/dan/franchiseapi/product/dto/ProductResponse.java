package com.dan.franchiseapi.product.dto;

public record ProductResponse(
        Long id,
        Long branchId,
        String name,
        Integer stock
) {
}
