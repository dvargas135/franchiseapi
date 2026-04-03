package com.dan.franchiseapi.report.dto;

public record TopStockProductResponse(
        Long branchId,
        String branchName,
        Long productId,
        String productName,
        Integer stock
) {
}
