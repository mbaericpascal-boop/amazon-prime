package com.keyce.amazon_prime.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_email", "film_id"}))
public class Notation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email de l'utilisateur qui note
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    // Film noté
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    // Note de 1 à 5
    @Column(nullable = false)
    private int note;

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }

    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }
}