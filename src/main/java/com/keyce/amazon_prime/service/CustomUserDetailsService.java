package com.keyce.amazon_prime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.keyce.amazon_prime.model.Admin;
import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.repository.AdminRepository;
import com.keyce.amazon_prime.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // Spring Security appelle cette méthode au moment du login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // On cherche d'abord dans les admins
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return new org.springframework.security.core.userdetails.User(
                admin.getEmail(),
                admin.getMotDePasse(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        // Sinon on cherche dans les users
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Compte introuvable : " + email));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getMotDePasse(),
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}