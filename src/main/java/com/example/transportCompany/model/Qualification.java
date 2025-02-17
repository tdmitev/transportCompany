package com.example.transportCompany.model;

import jakarta.persistence.*;

@Entity
@Table(name = "qualification")
public class Qualification extends BaseEntity{
    // "HAZMAT", "FLAMMABLE", "PASSENGERS_OVER_12", "HEAVY_CARGO"
    @Column(nullable = false, length = 50, unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}