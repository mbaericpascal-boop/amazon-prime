package com.keyce.amazon_prime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.service.FilmService;
import com.keyce.amazon_prime.service.UserService;

@Controller
public class CatalogueController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    // Catalogue principal
    @GetMapping("/catalogue")
    public String catalogue(Authentication auth,
                            @RequestParam(required = false) String genre,
                            @RequestParam(required = false) String recherche,
                            Model model) {

        // Récupérer le type d'abonnement de l'utilisateur connecté
        String email = auth.getName();
        User user = userService.trouverParEmail(email).orElse(null);
        String abonnement = (user != null) ? user.getTypeAbonnement() : "BASIC";

        List<Film> films;

        if (recherche != null && !recherche.isEmpty()) {
            films = filmService.rechercherParTitre(recherche);
        } else if (genre != null && !genre.isEmpty()) {
            films = filmService.filtrerParGenre(genre);
        } else {
            films = filmService.filmsDisponibles(abonnement);
        }

        model.addAttribute("films", films);
        model.addAttribute("user", user);
        model.addAttribute("abonnement", abonnement);
        model.addAttribute("genreSelectionne", genre);
        model.addAttribute("recherche", recherche);

        return "catalogue";
    }

    // Détail d'un film
    @GetMapping("/film/{id}")
    public String detailFilm(@PathVariable Long id,
                              Authentication auth,
                              Model model) {

        Film film = filmService.trouverParId(id)
            .orElseThrow(() -> new RuntimeException("Film introuvable."));

        String email = auth.getName();
        User user = userService.trouverParEmail(email).orElse(null);
        String abonnement = (user != null) ? user.getTypeAbonnement() : "BASIC";

        // Vérifier si l'utilisateur a accès à ce film
        boolean acces = switch (abonnement) {
            case "PREMIUM" -> true;
            case "STANDARD" -> !film.getAbonnementRequis().equals("PREMIUM");
            default -> film.getAbonnementRequis().equals("BASIC");
        };

        model.addAttribute("film", film);
        model.addAttribute("acces", acces);
        model.addAttribute("abonnement", abonnement);

        return "film-detail";
    }

    // Page profil utilisateur
    @GetMapping("/profil")
    public String profil(Authentication auth, Model model) {
        String email = auth.getName();
        User user = userService.trouverParEmail(email).orElse(null);
        model.addAttribute("user", user);
        return "profil";
    }
}