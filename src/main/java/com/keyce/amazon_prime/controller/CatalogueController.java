package com.keyce.amazon_prime.controller;

import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.service.FilmService;
import com.keyce.amazon_prime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CatalogueController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    @GetMapping("/catalogue")
    public String catalogue(Authentication auth,
                            @RequestParam(required = false) String genre,
                            @RequestParam(required = false) String recherche,
                            Model model) {
        String email = auth.getName();
        User user = userService.trouverParEmail(email).orElse(null);
        String abonnement = (user != null) ? user.getTypeAbonnement() : "BASIC";

        List<Film> films;
        if (recherche != null && !recherche.isEmpty()) {
            films = filmService.rechercherParTitre(recherche);
        } else if (genre != null && !genre.isEmpty()) {
            films = filmService.filtrerParGenre(genre);
        } else {
            // On affiche tout le catalogue, mais certains seront verrouillés
            films = filmService.listerTous();
        }

        model.addAttribute("films", films);
        model.addAttribute("user", user);
        model.addAttribute("abonnementActuel", abonnement);
        return "catalogue";
    }

    @GetMapping("/film/{id}")
    public String detailFilm(@PathVariable Long id, Authentication auth, Model model) {
        Film film = filmService.trouverParId(id).orElseThrow(() -> new RuntimeException("Film non trouvé"));
        
        String email = auth.getName();
        User user = userService.trouverParEmail(email).orElse(null);
        String abonnement = (user != null) ? user.getTypeAbonnement() : "BASIC";

        // Logique de droits d'accès
        boolean aAcces = switch (abonnement) {
            case "PREMIUM" -> true;
            case "STANDARD" -> !film.getAbonnementRequis().equals("PREMIUM");
            default -> film.getAbonnementRequis().equals("BASIC");
        };

        model.addAttribute("film", film);
        model.addAttribute("aAcces", aAcces);
        return "film-detail";
    }
}