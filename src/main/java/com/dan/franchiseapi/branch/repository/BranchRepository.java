package com.dan.franchiseapi.branch.repository;

import com.dan.franchiseapi.branch.model.Branch;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveCrudRepository<Branch, Long> {

    Mono<Boolean> existsByFranchiseIdAndName(Long franchiseId, String name);

    Flux<Branch> findByFranchiseId(Long franchiseId);

    Mono<Branch> findByIdAndFranchiseId(Long id, Long franchiseId);

}
