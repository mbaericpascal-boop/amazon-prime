package com.keyce.amazon_prime.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Remplacement du lien User par une simple chaîne pour l'identifier
    private String username; 

    private String reference;
    private Double montant;
    private String formule;
    private String modePaiement;
    private String numeroTelephone;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;

    private LocalDateTime dateCreation;

    public Transaction() {}

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }

    // ===== GETTERS ET SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public String getFormule() { return formule; }
    public void setFormule(String formule) { this.formule = formule; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public String getNumeroTelephone() { return numeroTelephone; }
    public void setNumeroTelephone(String numeroTelephone) { this.numeroTelephone = numeroTelephone; }

    public StatutPaiement getStatut() { return statut; }
    public void setStatut(StatutPaiement statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}

enum StatutPaiement {
    PENDING, SUCCESS, FAILED
}