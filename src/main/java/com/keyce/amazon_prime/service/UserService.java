package com.keyce.amazon_prime.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inscrire un nouvel utilisateur
    public User inscrire(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }
        // On chiffre le mot de passe avant de sauvegarder
        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
        return userRepository.save(user);
    }

    // Trouver un user par email
    public Optional<User> trouverParEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Trouver un user par ID
    public Optional<User> trouverParId(Long id) {
        return userRepository.findById(id);
    }

    // Lister tous les users (pour l'admin)
    public List<User> listerTous() {
        return userRepository.findAll();
    }

    // Modifier l'abonnement d'un user
    public User modifierAbonnement(Long id, String nouvelAbonnement) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
        user.setTypeAbonnement(nouvelAbonnement);
        return userRepository.save(user);
    }

    // Supprimer un user (pour l'admin)
    public void supprimer(Long id) {
        userRepository.deleteById(id);
    }
}