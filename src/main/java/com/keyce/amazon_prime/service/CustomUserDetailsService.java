package com.keyce.amazon_prime.service;

import com.keyce.amazon_prime.model.Admin;
import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.repository.AdminRepository;
import com.keyce.amazon_prime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Contrôle d'accès pour le rôle Administrateur
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return org.springframework.security.core.userdetails.User.withUsername(admin.getEmail())
                .password(admin.getMotDePasse())
                .disabled(false)
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();
        }

        // 2. Contrôle d'accès pour les utilisateurs
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Compte introuvable : " + email));

        // Un utilisateur n'ayant pas payé garde l'attribut "EN_ATTENTE" qui désactive son authentification
        boolean compteBloque = "EN_ATTENTE".equalsIgnoreCase(user.getTypeAbonnement());

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
            .password(user.getMotDePasse())
            .disabled(compteBloque) // True bloque l'accès, False valide la connexion
            .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
            .build();
    }
}