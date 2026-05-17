package com.keyce.amazon_prime.controller;

import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.service.FilmService;
import com.keyce.amazon_prime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private FilmService filmService;
    @Autowired(required = false) private JavaMailSender mailSender; // Sécurisé si pas encore configuré

    @GetMapping("/")
    public String accueil(Authentication auth, Model model) {
        boolean completementAuthentifie = (auth != null && auth.isAuthenticated());
        model.addAttribute("estConnecte", completementAuthentifie);
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
        if (inscrit != null)    model.addAttribute("message", "Abonnement activé avec succès ! Connectez-vous.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("nom") String nom,
            @RequestParam("email") String email,
            @RequestParam("motDePasse") String motDePasse,
            @RequestParam("typeAbonnement") String typeAbonnement,
            @RequestParam("modePaiement") String modePaiement,
            @RequestParam(value = "telephone", required = false) String telephone,
            @RequestParam(value = "paypalEmail", required = false) String paypalEmail,
            @RequestParam("referenceTransaction") String referenceTransaction,
            Model model) {

        boolean paiementValide = false;

        // 1. Validation stricte du paiement Mobile Money Cameroun (9 chiffres, commençant par 65, 67, 68, 69)
        if (modePaiement.equals("ORANGE_MONEY") || modePaiement.equals("MTN_MOMO")) {
            if (telephone != null && telephone.trim().matches("^(65|67|68|69)\\d{7}$")) {
                paiementValide = !referenceTransaction.trim().isEmpty();
            }
        } 
        // 2. Validation PayPal
        else if (modePaiement.equals("PAYPAL")) {
            if (paypalEmail != null && paypalEmail.contains("@")) {
                paiementValide = !referenceTransaction.trim().isEmpty();
            }
        }

        if (!paiementValide) {
            model.addAttribute("erreur", "Échec du paiement. Le numéro de téléphone mobile (CM) ou l'identifiant de transaction est invalide.");
            return "auth/register";
        }

        try {
            // 3. Le paiement est bon, on crée l'utilisateur en BDD
            User newUser = new User();
            newUser.setNom(nom);
            newUser.setEmail(email);
            newUser.setMotDePasse(motDePasse);
            newUser.setTypeAbonnement(typeAbonnement.toUpperCase());
            userService.inscrire(newUser);

            // 4. Envoi de l'e-mail de confirmation à l'image de Netflix
            if (mailSender != null) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("noreply@camstream.cm");
                    message.setTo(email);
                    message.setSubject("Bienvenue sur CamStream ! Votre abonnement est activé.");
                    message.setText("Bonjour " + nom + ",\n\n" +
                            "Le paiement de votre forfait " + typeAbonnement.toUpperCase() + " a été validé avec succès via " + modePaiement.replace("_", " ") + ".\n" +
                            "Référence de transaction : " + referenceTransaction + "\n\n" +
                            "Votre compte est désormais pleinement opérationnel. Vous pouvez dès à présent vous connecter et profiter de l'intégralité de notre catalogue de films et séries au Cameroun.\n\n" +
                            "L'équipe de streaming CamStream.");
                    mailSender.send(message);
                } catch (Exception mailException) {
                    System.out.println("Avertissement : Le serveur de mail n'est pas encore configuré, mais l'utilisateur est inscrit.");
                }
            }

            return "redirect:/login?inscrit=true";

        } catch (Exception e) {
            model.addAttribute("erreur", "Cet e-mail est déjà associé à un compte actif.");
            return "auth/register";
        }
    }
}