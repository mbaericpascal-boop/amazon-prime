package com.keyce.amazon_prime.controller;

import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.service.FilmService;
import com.keyce.amazon_prime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    @GetMapping("/")
    public String accueil(Authentication auth, Model model) {
        // On vérifie si l'utilisateur est connecté
        boolean completementAuthentifie = (auth != null && auth.isAuthenticated());
        
        // On envoie l'information à index.html pour que le JavaScript sache s'il doit rediriger après le splash screen
        model.addAttribute("estConnecte", completementAuthentifie);
        
        // Pour la "prévisualisation" sur la page d'accueil (le mur 3D)
        model.addAttribute("films", filmService.listerTous());
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
        try {
            if (user.getTypeAbonnement() == null || user.getTypeAbonnement().isEmpty()) {
                user.setTypeAbonnement("BASIC");
            }
            userService.inscrire(user);
            return "redirect:/login?inscrit=true";
        } catch (Exception e) {
            model.addAttribute("erreur", "Erreur lors de l'inscription. L'email est peut-être déjà utilisé.");
            return "auth/register";
        }
    }
}