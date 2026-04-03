package com.dan.franchiseapi.branch.mapper;

import com.dan.franchiseapi.branch.dto.BranchResponse;
import com.dan.franchiseapi.branch.dto.CreateBranchRequest;
import com.dan.franchiseapi.branch.model.Branch;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    public Branch toEntity(Long franchiseId, CreateBranchRequest request) {
        return new Branch(franchiseId, request.name());
    }

    public BranchResponse toResponse(Branch branch) {
        return new BranchResponse(
                branch.getId(),
                branch.getFranchiseId(),
                branch.getName()
        );
    }

}
