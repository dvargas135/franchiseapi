package com.dan.franchiseapi.franchise.repository;

import com.dan.franchiseapi.franchise.model.Franchise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveCrudRepository<Franchise, Long> {

    Mono<Boolean> existsByName(String name);

    Mono<Franchise> findByName(String name);

}
