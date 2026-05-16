package com.keyce.amazon_prime.controller;

import com.keyce.amazon_prime.model.User;
import com.keyce.amazon_prime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private UserRepository userRepository;

    // Étape 1 : Choix de la formule (Style Netflix/Amazon)
    @GetMapping("/offre")
    public String afficherOffres(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "payment-offre"; // Renvoie vers templates/payment-offre.html
    }

    // Étape 2 : Page de saisie des coordonnées financières (Checkout)
    @GetMapping("/checkout")
    public String afficherCheckout(@RequestParam("formule") String formule, 
                                   @RequestParam("email") String email, 
                                   Model model) {
        double prix = 0;
        if (formule.equalsIgnoreCase("basic")) prix = 2000;
        else if (formule.equalsIgnoreCase("standard")) prix = 4500;
        else if (formule.equalsIgnoreCase("premium")) prix = 7500;

        model.addAttribute("formule", formule.toUpperCase());
        model.addAttribute("prix", prix);
        model.addAttribute("email", email);
        return "checkout"; // Renvoie vers templates/checkout.html
    }

    // Étape 3 : Traitement du paiement et activation instantanée
    @PostMapping("/process")
    public String traiterPaiement(@RequestParam("formule") String formule,
                                  @RequestParam("montant") Double montant,
                                  @RequestParam("modePaiement") String modePaiement,
                                  @RequestParam("email") String email,
                                  @RequestParam(value = "telephone", required = false) String telephone,
                                  @RequestParam(value = "numeroCarte", required = false) String numeroCarte,
                                  Model model) {
        
        boolean paiementReussi = true; 

        // Validation dynamique des inputs selon le moyen sélectionné
        if (modePaiement.equals("ORANGE_MONEY") || modePaiement.equals("MTN_MOMO")) {
            if (telephone == null || telephone.trim().length() < 9) {
                paiementReussi = false;
            }
        } else if (modePaiement.equals("CARTE_BANCAIRE")) {
            if (numeroCarte == null || numeroCarte.trim().length() < 12) {
                paiementReussi = false;
            }
        } else {
            paiementReussi = false;
        }

        if (paiementReussi) {
            // Recherche de l'utilisateur par son email pour appliquer l'abonnement payé
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                user.setTypeAbonnement(formule.toUpperCase()); // Libère le compte (Ex: BASIC, PREMIUM)
                userRepository.save(user);
            }
            
            model.addAttribute("message", "Félicitations ! Votre paiement de " + montant + " FCFA via " + modePaiement.replace("_", " ") + " a été validé. Vos 7 jours d'essai gratuit commencent dès maintenant.");
            return "payment-success"; // Renvoie vers templates/payment-success.html
        } else {
            model.addAttribute("erreur", "Échec de l'opération. Veuillez vérifier la conformité de vos identifiants ou votre solde.");
            model.addAttribute("formule", formule.toUpperCase());
            model.addAttribute("prix", montant);
            model.addAttribute("email", email);
            return "checkout";
        }
    }
}