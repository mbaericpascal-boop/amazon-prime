package com.keyce.amazon_prime.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.repository.FilmRepository;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    // Niveaux d'abonnement (du plus bas au plus haut)
    // BASIC peut voir BASIC seulement
    // STANDARD peut voir BASIC + STANDARD
    // PREMIUM peut voir tout
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

    // Modifier un film (admin)
    public Film modifier(Long id, Film filmModifie) {
        Film film = filmRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Film introuvable."));
        film.setTitre(filmModifie.getTitre());
        film.setGenre(filmModifie.getGenre());
        film.setAnnee(filmModifie.getAnnee());
        film.setDescription(filmModifie.getDescription());
        film.setAbonnementRequis(filmModifie.getAbonnementRequis());
        return filmRepository.save(film);
    }

    // Supprimer un film (admin)
    public void supprimer(Long id) {
        filmRepository.deleteById(id);
    }
}