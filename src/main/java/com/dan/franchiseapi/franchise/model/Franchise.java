package com.dan.franchiseapi.franchise.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("franchises")
public class Franchise {

    @Id
    private Long id;
    private String name;

    public Franchise() {
    }

    public Franchise(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Franchise(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
