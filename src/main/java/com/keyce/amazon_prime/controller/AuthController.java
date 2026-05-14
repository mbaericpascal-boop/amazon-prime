package com.keyce.amazon_prime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Page d'accueil
    @GetMapping("/")
    public String accueil() {
        return "index";
    }

    // Page login
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String erreur,
                        @RequestParam(required = false) String deconnecte,
                        Model model) {
        if (erreur != null) {
            model.addAttribute("erreur", "Email ou mot de passe incorrect.");
        }
        if (deconnecte != null) {
            model.addAttribute("message", "Vous avez été déconnecté avec succès.");
        }
        return "auth/login";
    }

    // Page inscription
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // Traitement du formulaire d'inscription
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            // Abonnement BASIC par défaut
            if (user.getTypeAbonnement() == null || user.getTypeAbonnement().isEmpty()) {
                user.setTypeAbonnement("BASIC");
            }
            userService.inscrire(user);
            return "redirect:/login?inscrit=true";
        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage());
            return "auth/register";
        }
    }
}