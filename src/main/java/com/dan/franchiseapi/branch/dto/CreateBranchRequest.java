package com.dan.franchiseapi.branch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBranchRequest(
        @NotBlank(message = "Branch name is required")
        @Size(max = 150, message = "Branch name must not exceed 150 characters")
        String name
) {
}
