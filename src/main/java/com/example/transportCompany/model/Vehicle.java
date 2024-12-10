package com.example.transportCompany.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "vehicle")
public class Vehicle extends BaseEntity{

    @Column(nullable = false, length = 20)
    private String licensePlate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal fuelConsumption;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private TransportCompany company;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private VehicleType vehicleType;

    @OneToMany(mappedBy = "vehicle")
    private List<Transport> transports;

    // Getters and Setters

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public BigDecimal getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(BigDecimal fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public TransportCompany getCompany() {
        return company;
    }

    public void setCompany(TransportCompany company) {
        this.company = company;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<Transport> getTransports() {
        return transports;
    }

    public void setTransports(List<Transport> transports) {
        this.transports = transports;
    }
}