package com.keyce.amazon_prime.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.keyce.amazon_prime.model.Film;
import com.keyce.amazon_prime.model.Notation;

@Repository
public interface NotationRepository extends JpaRepository<Notation, Long> {

    // Trouver la note d'un user pour un film précis
    Optional<Notation> findByUserEmailAndFilm(String userEmail, Film film);

    // Toutes les notes d'un film
    List<Notation> findByFilm(Film film);

    // Moyenne des notes d'un film
    @Query("SELECT AVG(n.note) FROM Notation n WHERE n.film = :film")
    Double moyenneParFilm(@Param("film") Film film);

    // Nombre de votes d'un film
    long countByFilm(Film film);
}