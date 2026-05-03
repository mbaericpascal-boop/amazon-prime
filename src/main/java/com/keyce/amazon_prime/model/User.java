package com.keyce.amazon_prime.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends Personne {

    @Column(nullable = false)
    private String typeAbonnement; // "BASIC", "STANDARD", "PREMIUM"

    public User() {}

    public String getTypeAbonnement() { return typeAbonnement; }
    public void setTypeAbonnement(String t) { this.typeAbonnement = t; }

    @Override
    public String getRole() { return "USER"; }
}