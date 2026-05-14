package com.keyce.amazon_prime.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keyce.amazon_prime.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Trouver un admin par email (pour le login)
    Optional<Admin> findByEmail(String email);

    // Vérifier si un email admin existe déjà
    boolean existsByEmail(String email);
}