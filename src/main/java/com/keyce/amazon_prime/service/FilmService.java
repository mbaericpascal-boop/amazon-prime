package com.keyce.amazon_prime.service;

import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    // Niveaux d'abonnement (du plus bas au plus haut)
    public List<Film> filmsDisponibles(String typeAbonnement) {
        List<Film> tous = filmRepository.findAll();
        return tous.stream().filter(film -> {
            String requis = film.getAbonnementRequis();
            return switch (typeAbonnement) {
                case "PREMIUM" -> true;
                case "STANDARD" -> requis.equals("BASIC") || requis.equals("STANDARD");
                default -> requis.equals("BASIC"); // BASIC
            };
        }).toList();
    }

    // Tous les films (pour l'admin)
    public List<Film> listerTous() {
        return filmRepository.findAll();
    }

    // Trouver par ID
    public Optional<Film> trouverParId(Long id) {
        return filmRepository.findById(id);
    }

    // Rechercher par titre
    public List<Film> rechercherParTitre(String titre) {
        return filmRepository.findByTitreContainingIgnoreCase(titre);
    }

    // Filtrer par genre
    public List<Film> filtrerParGenre(String genre) {
        return filmRepository.findByGenre(genre);
    }

    // Ajouter un film (admin)
    public Film ajouter(Film film) {
        return filmRepository.save(film);
    }

    // Modifier un film (admin) mis à jour
    public Film modifier(Long id, Film filmModifie) {
        Film film = filmRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Film introuvable."));
        film.setTitre(filmModifie.getTitre());
        film.setGenre(filmModifie.getGenre());
        film.setAnnee(filmModifie.getAnnee());
        film.setDescription(filmModifie.getDescription());
        film.setAbonnementRequis(filmModifie.getAbonnementRequis());
        
        // Synchronisation des nouveaux champs
        film.setImageUri(filmModifie.getImageUri());
        film.setAfficheBanniere(filmModifie.getAfficheBanniere());
        film.setTrailerUrl(filmModifie.getTrailerUrl());
        
        return filmRepository.save(film);
    }

    // Supprimer un film (admin)
    public void supprimer(Long id) {
        filmRepository.deleteById(id);
    }
}