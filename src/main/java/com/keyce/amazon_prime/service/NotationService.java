package com.keyce.amazon_prime.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.model.Notation;
import com.keyce.amazon_prime.repository.FilmRepository;
import com.keyce.amazon_prime.repository.NotationRepository;

@Service
public class NotationService {

    @Autowired
    private NotationRepository notationRepository;

    @Autowired
    private FilmRepository filmRepository;

    // Noter ou mettre à jour la note d'un film
    public void noter(String userEmail, Long filmId, int note) {
        if (note < 1 || note > 5) {
            throw new RuntimeException("La note doit être entre 1 et 5.");
        }
        Film film = filmRepository.findById(filmId)
            .orElseThrow(() -> new RuntimeException("Film introuvable."));

        // Si l'user a déjà noté ce film → on met à jour
        Optional<Notation> existante = notationRepository.findByUserEmailAndFilm(userEmail, film);
        Notation notation = existante.orElse(new Notation());
        notation.setUserEmail(userEmail);
        notation.setFilm(film);
        notation.setNote(note);
        notationRepository.save(notation);
    }

    // Récupérer la moyenne d'un film (arrondie à 1 décimale)
    public double moyenneFilm(Long filmId) {
        Film film = filmRepository.findById(filmId).orElse(null);
        if (film == null) return 0.0;
        Double moyenne = notationRepository.moyenneParFilm(film);
        if (moyenne == null) return 0.0;
        return Math.round(moyenne * 10.0) / 10.0;
    }

    // Nombre de votes pour un film
    public long nbVotes(Long filmId) {
        Film film = filmRepository.findById(filmId).orElse(null);
        if (film == null) return 0;
        return notationRepository.countByFilm(film);
    }

    // Note donnée par un user pour un film (0 si pas encore noté)
    public int noteUtilisateur(String userEmail, Long filmId) {
        Film film = filmRepository.findById(filmId).orElse(null);
        if (film == null) return 0;
        return notationRepository.findByUserEmailAndFilm(userEmail, film)
            .map(Notation::getNote)
            .orElse(0);
    }
}