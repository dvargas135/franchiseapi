package com.dan.franchiseapi.franchise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFranchiseNameRequest(
        @NotBlank(message = "Franchise name is required")
        @Size(max = 150, message = "Franchise name must not exceed 150 characters")
        String name
) {
}
