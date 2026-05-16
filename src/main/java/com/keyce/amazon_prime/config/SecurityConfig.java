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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authProvider())
            .authorizeHttpRequests(auth -> auth

                // Pages publiques, ressources statiques et parcours de paiement autorisés sans session
                .requestMatchers("/", "/login", "/register", "/payment/**", "/css/**", "/js/**", "/images/**", "/videos/**", "/video/**").permitAll()

                // Pages réservées à l'administration
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Tout le reste (Le catalogue, les profils...) nécessite une connexion active et validée
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")             
                .loginProcessingUrl("/login")    
                .usernameParameter("email")      
                .passwordParameter("motDePasse")
                .defaultSuccessUrl("/catalogue", true)  
                .failureUrl("/login?erreur=true")        
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?deconnecte=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Désactivation CSRF ciblée pour l'usage fluide de la console de développement H2
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/payment/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}