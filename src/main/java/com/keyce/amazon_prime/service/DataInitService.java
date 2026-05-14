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

        // --- Admin par défaut ---
        if (!adminRepository.existsByEmail("admin@keyce.cm")) {
            Admin admin = new Admin();
            admin.setNom("Administrateur KEYCE");
            admin.setEmail("admin@keyce.cm");
            admin.setMotDePasse(passwordEncoder.encode("Admin2024!"));
            admin.setDepartement("Informatique");
            adminRepository.save(admin);
        }

        // --- Users de démo (contexte camerounais) ---
        if (!userRepository.existsByEmail("jean@gmail.com")) {
            User u1 = new User();
            u1.setNom("Jean Mbarga");
            u1.setEmail("jean@gmail.com");
            u1.setMotDePasse(passwordEncoder.encode("password123"));
            u1.setTypeAbonnement("BASIC");
            userRepository.save(u1);
        }
        if (!userRepository.existsByEmail("fatima@gmail.com")) {
            User u2 = new User();
            u2.setNom("Fatima Ngassa");
            u2.setEmail("fatima@gmail.com");
            u2.setMotDePasse(passwordEncoder.encode("password123"));
            u2.setTypeAbonnement("STANDARD");
            userRepository.save(u2);
        }
        if (!userRepository.existsByEmail("paul@gmail.com")) {
            User u3 = new User();
            u3.setNom("Paul Tchamba");
            u3.setEmail("paul@gmail.com");
            u3.setMotDePasse(passwordEncoder.encode("password123"));
            u3.setTypeAbonnement("PREMIUM");
            userRepository.save(u3);
        }

        // --- Films de démo ---
        if (filmRepository.count() == 0) {
            // Films africains / contexte local
            creerFilm("Mboa", "Drame", 2022, "BASIC",
                "Une histoire poignante sur la vie à Douala, entre tradition et modernité.");
            creerFilm("Le Retour au Village", "Comédie", 2021, "BASIC",
                "Un jeune de Yaoundé rentre au village pour les fêtes — et tout s'enchaîne !");
            creerFilm("Nollywood Nights", "Romance", 2023, "STANDARD",
                "Une co-production Cameroun-Nigeria sur un amour impossible entre deux familles rivales.");
            creerFilm("Black Panther", "Action", 2018, "STANDARD",
                "Le roi du Wakanda défend son royaume face à un ennemi venu de loin.");
            creerFilm("Wakanda Forever", "Action", 2022, "PREMIUM",
                "Le Wakanda fait face à une nouvelle menace après la disparition de son roi.");
            creerFilm("La Pirogue", "Aventure", 2012, "BASIC",
                "Des pêcheurs sénégalais risquent leur vie pour rejoindre l'Europe.");
            creerFilm("Timbuktu", "Drame", 2014, "STANDARD",
                "Un berger résiste aux djihadistes qui ont pris possession de Tombouctou.");
            creerFilm("Night of the Kings", "Drame", 2020, "PREMIUM",
                "Dans une prison ivoirienne, un jeune détenu doit raconter des histoires toute la nuit.");
        }
    }

    private void creerFilm(String titre, String genre, int annee,
                            String abonnement, String description) {
        Film f = new Film();
        f.setTitre(titre);
        f.setGenre(genre);
        f.setAnnee(annee);
        f.setAbonnementRequis(abonnement);
        f.setDescription(description);
        filmRepository.save(f);
    }
}