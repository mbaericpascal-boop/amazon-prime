package com.keyce.amazon_prime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.keyce.amazon_prime.model.User;

import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.service.UserService;
import com.keyce.amazon_prime.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String accueil() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String erreur,
                        @RequestParam(required = false) String deconnecte,
                        @RequestParam(required = false) String inscrit,
                        Model model) {
        if (erreur != null)     model.addAttribute("erreur", "Email ou mot de passe incorrect.");
        if (deconnecte != null) model.addAttribute("message", "Vous avez été déconnecté.");
        if (inscrit != null)    model.addAttribute("message", "Compte créé ! Connectez-vous.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        // Validation manuelle simple
        if (user.getNom() == null || user.getNom().trim().isEmpty()) {
            model.addAttribute("erreur", "Le nom est obligatoire.");
            return "auth/register";
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            model.addAttribute("erreur", "L'email est obligatoire.");
            return "auth/register";
        }
        if (user.getMotDePasse() == null || user.getMotDePasse().length() < 6) {
            model.addAttribute("erreur", "Le mot de passe doit contenir au moins 6 caractères.");
            return "auth/register";
        }
        try {
            if (user.getTypeAbonnement() == null || user.getTypeAbonnement().isEmpty()) {
                user.setTypeAbonnement("BASIC");
            }
            userService.inscrire(user);
            return "redirect:/login?inscrit=true";
        } catch (Exception e) {
            // Email déjà utilisé ou autre erreur
            model.addAttribute("erreur", "Cet email est déjà utilisé. Essayez avec un autre.");
            model.addAttribute("user", user);
            return "auth/register";
        }
    }
}