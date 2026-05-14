package com.keyce.amazon_prime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.service.FilmService;
import com.keyce.amazon_prime.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    // Dashboard admin
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalFilms", filmService.listerTous().size());
        model.addAttribute("totalUsers", userService.listerTous().size());
        model.addAttribute("films", filmService.listerTous());
        model.addAttribute("users", userService.listerTous());
        return "admin/dashboard";
    }

    // ===== GESTION DES FILMS =====

    // Formulaire ajout film
    @GetMapping("/films/nouveau")
    public String nouveauFilmForm(Model model) {
        model.addAttribute("film", new Film());
        return "admin/film-form";
    }

    // Traitement ajout film
    @PostMapping("/films/nouveau")
    public String ajouterFilm(@ModelAttribute Film film) {
        filmService.ajouter(film);
        return "redirect:/admin/dashboard?filmAjoute=true";
    }

    // Formulaire modification film
    @GetMapping("/films/modifier/{id}")
    public String modifierFilmForm(@PathVariable Long id, Model model) {
        Film film = filmService.trouverParId(id)
            .orElseThrow(() -> new RuntimeException("Film introuvable."));
        model.addAttribute("film", film);
        return "admin/film-form";
    }

    // Traitement modification film
    @PostMapping("/films/modifier/{id}")
    public String modifierFilm(@PathVariable Long id, @ModelAttribute Film film) {
        filmService.modifier(id, film);
        return "redirect:/admin/dashboard?filmModifie=true";
    }

    // Supprimer un film
    @PostMapping("/films/supprimer/{id}")
    public String supprimerFilm(@PathVariable Long id) {
        filmService.supprimer(id);
        return "redirect:/admin/dashboard?filmSupprime=true";
    }

    // ===== GESTION DES UTILISATEURS =====

    // Changer abonnement d'un user
    @PostMapping("/users/abonnement/{id}")
    public String modifierAbonnement(@PathVariable Long id,
                                      @RequestParam String abonnement) {
        userService.modifierAbonnement(id, abonnement);
        return "redirect:/admin/dashboard?abonnementModifie=true";
    }

    // Supprimer un utilisateur
    @PostMapping("/users/supprimer/{id}")
    public String supprimerUser(@PathVariable Long id) {
        userService.supprimer(id);
        return "redirect:/admin/dashboard?userSupprime=true";
    }
}