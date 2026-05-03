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

    private String description;

    public Film() {}

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
}