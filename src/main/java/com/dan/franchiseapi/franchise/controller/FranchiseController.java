package com.dan.franchiseapi.franchise.controller;

import com.dan.franchiseapi.franchise.dto.CreateFranchiseRequest;
import com.dan.franchiseapi.franchise.dto.FranchiseResponse;
import com.dan.franchiseapi.franchise.dto.UpdateFranchiseNameRequest;
import com.dan.franchiseapi.franchise.service.FranchiseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(@Valid @RequestBody CreateFranchiseRequest request) {
        return franchiseService.createFranchise(request);
    }

    @PatchMapping("/{franchiseId}/name")
    public Mono<FranchiseResponse> updateFranchiseName(
            @PathVariable Long franchiseId,
            @Valid @RequestBody UpdateFranchiseNameRequest request
    ) {
        return franchiseService.updateFranchiseName(franchiseId, request);
    }

}
