package com.keyce.amazon_prime.service;

import com.keyce.amazon_prime.model.Admin;
import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.repository.AdminRepository;
import com.keyce.amazon_prime.repository.FilmRepository;
import com.keyce.amazon_prime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitService implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private FilmRepository filmRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // --- Administrateur ---
        if (!adminRepository.existsByEmail("admin@keyce.cm")) {
            Admin admin = new Admin();
            admin.setNom("Administrateur KEYCE");
            admin.setEmail("admin@keyce.cm");
            admin.setMotDePasse(passwordEncoder.encode("Admin2024!"));
            admin.setDepartement("Informatique");
            adminRepository.save(admin);
        }

        // --- Utilisateur de démo ---
        if (!userRepository.existsByEmail("jean@gmail.com")) {
            User u1 = new User();
            u1.setNom("Jean Mbarga");
            u1.setEmail("jean@gmail.com");
            u1.setMotDePasse(passwordEncoder.encode("password123"));
            u1.setTypeAbonnement("BASIC");
            userRepository.save(u1);
        }

        // FORCE LE NETTOYAGE POUR PRENDRE EN COMPTE LES 13 FILMS
        filmRepository.deleteAll(); 
        
        // --- Insertion des 13 vrais films ---
        // Catégorie 1 : Films populaires au Cameroun
        creerFilm("Mboa", "Drame", 2022, "BASIC", "Une histoire poignante sur la vie à Douala, entre tradition et modernité.", "/images/Mboa.png");
        creerFilm("Bataille des chéries", "Drame", 2023, "STANDARD", "Intrigues, pouvoir et trahisons au cœur d'une rivalité amoureuse intense.", "/images/Bataille des chéries .png");
        creerFilm("La patrie d'abord", "Action", 2019, "STANDARD", "Un hommage plein d'action aux forces de défense engagées pour la paix.", "/images/La patrie d'abord.png");
        creerFilm("Le Retour au Village", "Comédie", 2021, "BASIC", "Un jeune diplômé quitte la ville pour redécouvrir ses racines de manière hilarante.", "/images/Le Retour au Village.png");
        creerFilm("Le Juste", "Action / Thriller", 2023, "PREMIUM", "Un homme intègre se dresse seul contre la corruption dans son quartier.", "/images/Le juste.png");
        creerFilm("Waka", "Drame", 2014, "BASIC", "Le parcours courageux d'une femme bien décidée à s'en sortir face aux épreuves.", "/images/Waka.png");

        // Catégorie 2 : Séries Cultes
        creerFilm("Le journal de Jennifa", "Comédie", 2022, "BASIC", "Les mésaventures délirantes d'une jeune fille pleine d'ambition.", "/images/Le journal de Jennifa.png");
        creerFilm("Pakgne", "Comédie", 2020, "BASIC", "Retrouvez les commérages et les sketchs cultes du duo incontournable.", "/images/Pakgne.png");
        creerFilm("L'accord", "Drame", 2022, "STANDARD", "Quand les secrets de famille et les pactes silencieux refont surface.", "/images/L'accord.png");
        creerFilm("Maideut", "Comédie Dramatique", 2021, "BASIC", "Intrigues amoureuses et quiproquos au sein de la bourgeoisie locale.", "/images/Maideut.png");
        creerFilm("The Plan", "Thriller", 2023, "PREMIUM", "Un braquage à haut risque où les trahisons s'enchaînent plus vite que prévu.", "/images/The plan.png");

        // Catégorie 3 : Pour les plus jeunes
        creerFilm("Aya de Yopougon", "Animation", 2013, "BASIC", "Chronique tendre et colorée de la vie à Abidjan à la fin des années 70.", "/images/Aya de Yopougon.png");
        creerFilm("Black Panther", "Action / Fantastique", 2018, "PREMIUM", "Le roi T'Challa défend le Wakanda et son héritage face à une menace mondiale.", "/images/BlackPanther.png");
    }

    private void creerFilm(String titre, String genre, int annee, String abonnement, String description, String imageUri) {
        Film f = new Film();
        f.setTitre(titre);
        f.setGenre(genre);
        f.setAnnee(annee);
        f.setAbonnementRequis(abonnement);
        f.setDescription(description);
        f.setImageUri(imageUri);
        f.setAfficheBanniere(imageUri); 
        f.setTrailerUrl("/videos/mboa.mp4");
        filmRepository.save(f);
    }
}