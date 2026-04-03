package com.dan.franchiseapi.product.repository;

import com.dan.franchiseapi.product.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    Mono<Boolean> existsByBranchIdAndName(Long branchId, String name);

    Flux<Product> findByBranchId(Long branchId);

    Mono<Product> findByIdAndBranchId(Long id, Long branchId);

}
