package com.dan.franchiseapi.branch.service;

import com.dan.franchiseapi.branch.dto.BranchResponse;
import com.dan.franchiseapi.branch.dto.CreateBranchRequest;
import com.dan.franchiseapi.branch.dto.UpdateBranchNameRequest;
import com.dan.franchiseapi.branch.mapper.BranchMapper;
import com.dan.franchiseapi.branch.model.Branch;
import com.dan.franchiseapi.branch.repository.BranchRepository;
import com.dan.franchiseapi.common.exception.ConflictException;
import com.dan.franchiseapi.common.exception.NotFoundException;
import com.dan.franchiseapi.franchise.service.FranchiseService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final FranchiseService franchiseService;

    public BranchService(
            BranchRepository branchRepository,
            BranchMapper branchMapper,
            FranchiseService franchiseService
    ) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.franchiseService = franchiseService;
    }

    public Mono<BranchResponse> createBranch(Long franchiseId, CreateBranchRequest request) {
        return franchiseService.getFranchiseById(franchiseId)
                .then(branchRepository.existsByFranchiseIdAndName(franchiseId, request.name()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ConflictException("Branch name already exists in this franchise"));
                    }

                    Branch branch = branchMapper.toEntity(franchiseId, request);
                    return branchRepository.save(branch)
                            .map(branchMapper::toResponse);
                });
    }

    public Mono<BranchResponse> updateBranchName(Long branchId, UpdateBranchNameRequest request) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found")))
                .flatMap(existingBranch ->
                        branchRepository.existsByFranchiseIdAndName(existingBranch.getFranchiseId(), request.name())
                                .flatMap(exists -> {
                                    if (exists && !existingBranch.getName().equals(request.name())) {
                                        return Mono.error(new ConflictException("Branch name already exists in this franchise"));
                                    }

                                    existingBranch.setName(request.name());
                                    return branchRepository.save(existingBranch);
                                })
                )
                .map(branchMapper::toResponse);
    }

    public Mono<Branch> getBranchById(Long branchId) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found")));
    }

    public Mono<Branch> getBranchByIdAndFranchiseId(Long branchId, Long franchiseId) {
        return branchRepository.findByIdAndFranchiseId(branchId, franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found for the given franchise")));
    }

}
