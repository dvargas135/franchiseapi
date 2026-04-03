package com.dan.franchiseapi.franchise.mapper;

import com.dan.franchiseapi.franchise.dto.CreateFranchiseRequest;
import com.dan.franchiseapi.franchise.dto.FranchiseResponse;
import com.dan.franchiseapi.franchise.model.Franchise;
import org.springframework.stereotype.Component;

@Component
public class FranchiseMapper {

    public Franchise toEntity(CreateFranchiseRequest request) {
        return new Franchise(request.name());
    }

    public FranchiseResponse toResponse(Franchise franchise) {
        return new FranchiseResponse(
                franchise.getId(),
                franchise.getName()
        );
    }

}
