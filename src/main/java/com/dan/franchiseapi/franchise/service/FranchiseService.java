package com.dan.franchiseapi.franchise.service;

import com.dan.franchiseapi.common.exception.ConflictException;
import com.dan.franchiseapi.common.exception.NotFoundException;
import com.dan.franchiseapi.franchise.dto.CreateFranchiseRequest;
import com.dan.franchiseapi.franchise.dto.FranchiseResponse;
import com.dan.franchiseapi.franchise.dto.UpdateFranchiseNameRequest;
import com.dan.franchiseapi.franchise.mapper.FranchiseMapper;
import com.dan.franchiseapi.franchise.model.Franchise;
import com.dan.franchiseapi.franchise.repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseMapper franchiseMapper;

    public FranchiseService(FranchiseRepository franchiseRepository, FranchiseMapper franchiseMapper) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseMapper = franchiseMapper;
    }

    public Mono<FranchiseResponse> createFranchise(CreateFranchiseRequest request) {
        return franchiseRepository.existsByName(request.name())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ConflictException("Franchise name already exists"));
                    }

                    Franchise franchise = franchiseMapper.toEntity(request);
                    return franchiseRepository.save(franchise)
                            .map(franchiseMapper::toResponse);
                });
    }

    public Mono<FranchiseResponse> updateFranchiseName(Long franchiseId, UpdateFranchiseNameRequest request) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found")))
                .flatMap(existingFranchise ->
                        franchiseRepository.existsByName(request.name())
                                .flatMap(exists -> {
                                    if (exists && !existingFranchise.getName().equals(request.name())) {
                                        return Mono.error(new ConflictException("Franchise name already exists"));
                                    }

                                    existingFranchise.setName(request.name());
                                    return franchiseRepository.save(existingFranchise);
                                })
                )
                .map(franchiseMapper::toResponse);
    }

    public Mono<Franchise> getFranchiseById(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found")));
    }

}
