package com.dan.franchiseapi.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(max = 150, message = "Product name must not exceed 150 characters")
        String name,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be greater than or equal to 0")
        Integer stock
) {
}
