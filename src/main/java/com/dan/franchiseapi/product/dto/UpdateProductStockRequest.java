package com.dan.franchiseapi.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProductStockRequest(
        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be greater than or equal to 0")
        Integer stock
) {
}
