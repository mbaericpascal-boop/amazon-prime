package com.keyce.amazon_prime.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrangeMoneyService {

    // ÉTAPE 1 & 2 : Simulation de l'initialisation du paiement (Obtention du payToken)
    public String initialiserPaiement(String telephone, double montant) {
        // Dans la vraie vie : Faire un POST vers https://api-s1.orange.cm/omcoreapis/1.0.2/mp/init
        // Pour la soutenance : On génère un jeton fictif immédiatement pour aller vite
        System.out.println("[Orange Money API] Initialisation paiement pour " + telephone + " d'un montant de " + montant + " FCFA");
        return "PAY-TOKEN-SIMULE-" + System.currentTimeMillis();
    }

    // ÉTAPE 3 : Simulation du déclenchement du push USSD sur le téléphone
    public boolean declencherPushUssd(String payToken) {
        // Dans la vraie vie : Faire un POST vers https://api-s1.orange.cm/omcoreapis/1.0.2/mp/pay
        // Pour la soutenance : On simule l'envoi de la notification pop-up sur le téléphone du client
        System.out.println("[Orange Money API] Push USSD envoyé avec le token : " + payToken);
        return true; 
    }
}