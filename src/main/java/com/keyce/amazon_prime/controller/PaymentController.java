package com.keyce.amazon_prime.controller;

import com.keyce.amazon_prime.entity.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @GetMapping("/checkout")
    public String afficherCheckout(@RequestParam("formule") String formule, Model model) {
        double prix = 0;
        if (formule.equalsIgnoreCase("basic")) prix = 2000;
        else if (formule.equalsIgnoreCase("standard")) prix = 4500;
        else if (formule.equalsIgnoreCase("premium")) prix = 7500;

        model.addAttribute("formule", formule.toUpperCase());
        model.addAttribute("prix", prix);
        return "checkout";
    }

    @PostMapping("/process")
    public String traiterPaiement(@RequestParam("formule") String formule,
                                  @RequestParam("montant") Double montant,
                                  @RequestParam("modePaiement") String modePaiement,
                                  @RequestParam(value = "telephone", required = false) String telephone,
                                  Model model) {
        
        boolean paiementReussi = true; 

        if (modePaiement.equals("ORANGE_MONEY") || modePaiement.equals("MTN_MOMO")) {
            if (telephone == null || telephone.trim().length() < 9) {
                paiementReussi = false;
            }
        }

        if (paiementReussi) {
            model.addAttribute("message", "Félicitations ! Votre paiement de " + montant + " FCFA a été validé. Votre forfait " + formule + " est actif.");
            return "payment-success";
        } else {
            model.addAttribute("erreur", "Le paiement a échoué. Veuillez vérifier votre numéro ou votre solde Mobile Money.");
            model.addAttribute("formule", formule.toUpperCase());
            model.addAttribute("prix", montant);
            return "checkout";
        }
    }
}