package com.dan.franchiseapi.branch.controller;

import com.dan.franchiseapi.branch.dto.BranchResponse;
import com.dan.franchiseapi.branch.dto.CreateBranchRequest;
import com.dan.franchiseapi.branch.dto.UpdateBranchNameRequest;
import com.dan.franchiseapi.branch.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/api/franchises/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchResponse> createBranch(
            @PathVariable Long franchiseId,
            @Valid @RequestBody CreateBranchRequest request
    ) {
        return branchService.createBranch(franchiseId, request);
    }

    @PatchMapping("/api/branches/{branchId}/name")
    public Mono<BranchResponse> updateBranchName(
            @PathVariable Long branchId,
            @Valid @RequestBody UpdateBranchNameRequest request
    ) {
        return branchService.updateBranchName(branchId, request);
    }

}
