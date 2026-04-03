package com.dan.franchiseapi.branch.dto;

public record BranchResponse(
        Long id,
        Long franchiseId,
        String name
) {
}
