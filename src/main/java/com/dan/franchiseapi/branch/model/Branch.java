package com.dan.franchiseapi.branch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("branches")
public class Branch {

    @Id
    private Long id;
    private Long franchiseId;
    private String name;

    public Branch() {
    }

    public Branch(Long id, Long franchiseId, String name) {
        this.id = id;
        this.franchiseId = franchiseId;
        this.name = name;
    }

    public Branch(Long franchiseId, String name) {
        this.franchiseId = franchiseId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
