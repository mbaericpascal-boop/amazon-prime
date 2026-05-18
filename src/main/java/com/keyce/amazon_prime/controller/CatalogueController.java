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
            films = filmService.listerTous();
        }

        model.addAttribute("films", films);
        model.addAttribute("user", user);
        model.addAttribute("abonnementActuel", abonnement);
        return "catalogue";
    }

    // Endpoint API renvoyant du JSON pour la recherche prédictive (Style Google)
    @GetMapping("/api/films/search")
    @ResponseBody
    public List<Film> searchFilmsAjax(@RequestParam("query") String query) {
        if (query == null || query.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return filmService.rechercherParTitre(query);
    }

    @GetMapping("/film/{id}")
    public String detailFilm(@PathVariable Long id, Authentication auth, Model model) {
        Film film = filmService.trouverParId(id).orElseThrow(() -> new RuntimeException("Film non trouvé"));
        
        String email = auth.getName();
        User user = userService.trouverParEmail(email).orElse(null);
        String abonnement = (user != null) ? user.getTypeAbonnement() : "BASIC";

        // Logique de droits d'accès selon le forfait
        boolean aAcces = switch (abonnement) {
            case "PREMIUM" -> true;
            case "STANDARD" -> !film.getAbonnementRequis().equals("PREMIUM");
            default -> film.getAbonnementRequis().equals("BASIC");
        };

        // Liaisons correctes avec les variables attendues par le fichier HTML
        model.addAttribute("film", film);
        model.addAttribute("acces", aAcces); 
        model.addAttribute("abonnement", abonnement); 
        
        return "film-detail";
    }
}