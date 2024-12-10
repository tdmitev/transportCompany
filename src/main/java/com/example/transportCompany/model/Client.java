package com.example.transportCompany.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
public class Client extends BaseEntity{

    @Column(nullable = false, length = 45)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @OneToMany(mappedBy = "client")
    private List<Transport> transports;

    @ManyToMany(mappedBy = "clients")
    private List<TransportCompany> transportCompanies;

    // Getters and Setters

    public List<TransportCompany> getTransportCompanies() {
        return transportCompanies;
    }

    public void setTransportCompanies(List<TransportCompany> transportCompanies) {
        this.transportCompanies = transportCompanies;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Transport> getTransports() {
        return transports;
    }

    public void setTransports(List<Transport> transports) {
        this.transports = transports;
    }
}