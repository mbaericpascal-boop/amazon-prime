package com.keyce.amazon_prime.model;

import jakarta.persistence.*;

@Entity
@Table(name = "films")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;
    
    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int annee;

    @Column(nullable = false)
    private String abonnementRequis; // "BASIC", "STANDARD", "PREMIUM"

    @Column(length = 1000)
    private String description;

    private String imageUri;        // Miniature pour la carte du film
    private String afficheBanniere; // Grande affiche pour le haut de l'écran
    private String trailerUrl;      // Lien vers l'extrait vidéo (.mp4)

    // Nouveaux champs pour les sous-titres demandés par le prof
    private String sousTitreFrUrl;  // Chemin vers le fichier .vtt français
    private String sousTitreEnUrl;  // Chemin vers le fichier .vtt anglais

    public Film() {}

    // Getters et Setters existants
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }

    public String getAbonnementRequis() { return abonnementRequis; }
    public void setAbonnementRequis(String a) { this.abonnementRequis = a; }

    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }

    // Getters et Setters pour la refonte visuelle
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public String getAfficheBanniere() { return afficheBanniere; }
    public void setAfficheBanniere(String afficheBanniere) { this.afficheBanniere = afficheBanniere; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    // Nouveaux Getters et Setters pour la gestion des sous-titres
    public String getSousTitreFrUrl() { return sousTitreFrUrl; }
    public void setSousTitreFrUrl(String sousTitreFrUrl) { this.sousTitreFrUrl = sousTitreFrUrl; }

    public String getSousTitreEnUrl() { return sousTitreEnUrl; }
    public void setSousTitreEnUrl(String sousTitreEnUrl) { this.sousTitreEnUrl = sousTitreEnUrl; }
}