package com.keyce.amazon_prime.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends Personne {

    @Column(nullable = false)
    private String departement;

    public Admin() {}

    public String getDepartement() { return departement; }
    public void setDepartement(String d) { this.departement = d; }

    @Override
    public String getRole() { return "ADMIN"; }}