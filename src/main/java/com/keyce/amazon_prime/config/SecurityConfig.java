package com.keyce.amazon_prime.config;

import com.keyce.amazon_prime.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Encodeur de mot de passe BCrypt (standard sécurisé)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Relie notre service de login à l'encodeur
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Règles d'accès aux pages
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authProvider())
            .authorizeHttpRequests(auth -> auth

                // Pages publiques (accessibles sans connexion)
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()

                // Pages réservées aux admins
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Toutes les autres pages nécessitent d'être connecté
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")             // Notre page de login personnalisée
                .loginProcessingUrl("/login")    // URL qui traite le formulaire
                .usernameParameter("email")      // On utilise l'email comme identifiant
                .passwordParameter("motDePasse")
                .defaultSuccessUrl("/catalogue", true)  // Redirection après login réussi
                .failureUrl("/login?erreur=true")        // Redirection si échec
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?deconnecte=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Autoriser la console H2 (base de données en dev)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}