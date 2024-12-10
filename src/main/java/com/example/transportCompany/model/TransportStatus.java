package com.example.transportCompany.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transport_status")
public class TransportStatus extends BaseEntity{
    // PLANNED, COMPLETED, CANCELED
    @Column(nullable = false, length = 20)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}