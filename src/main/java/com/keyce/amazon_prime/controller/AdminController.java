package com.keyce.amazon_prime.controller;

import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.service.FilmService;
import com.keyce.amazon_prime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    // Page d'accueil du panneau d'administration
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("films", filmService.listerTous());
        model.addAttribute("users", userService.listerTous()); 
        model.addAttribute("nouveauFilm", new Film()); 
        return "admin-dashboard";
    }

    // Ajouter un film
    @PostMapping("/film/ajouter")
    public String ajouterFilm(@ModelAttribute Film film) {
        // CORRECTION : Appel de la méthode .ajouter() présente dans ton FilmService
        filmService.ajouter(film); 
        return "redirect:/admin/dashboard?successAdd";
    }

    // Supprimer un film
    @GetMapping("/film/supprimer/{id}")
    public String supprimerFilm(@PathVariable Long id) {
        // CORRECTION : Appel de la méthode .supprimer() présente dans ton FilmService
        filmService.supprimer(id); 
        return "redirect:/admin/dashboard?successDeleteFilm";
    }

    // Modifier ou bloquer un utilisateur (Suppression)
    @GetMapping("/user/supprimer/{id}")
    public String supprimerUtilisateur(@PathVariable Long id) {
        // CORRECTION : Appel de la méthode .supprimer() présente dans ton UserService
        userService.supprimer(id); 
        return "redirect:/admin/dashboard?successDeleteUser";
    }
}