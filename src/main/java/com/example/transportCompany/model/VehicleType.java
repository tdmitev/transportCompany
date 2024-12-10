package com.example.transportCompany.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle_type")
public class VehicleType extends BaseEntity{
    // BUS, TRUCK, CISTERN
    @Column(nullable = false, length = 45)
    private String name;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
