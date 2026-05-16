package com.keyce.amazon_prime.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keyce.amazon_prime.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trouver un user par email (pour le login)
    Optional<User> findByEmail(String email);

    // Vérifier si un email existe déjà (pour l'inscription)
    boolean existsByEmail(String email);
}