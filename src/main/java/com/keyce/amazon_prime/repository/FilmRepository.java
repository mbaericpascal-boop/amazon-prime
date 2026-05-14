package com.keyce.amazon_prime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keyce.amazon_prime.model.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    // Trouver les films par genre (ex: "Action", "Comédie")
    List<Film> findByGenre(String genre);

    // Trouver les films accessibles selon l'abonnement
    List<Film> findByAbonnementRequis(String abonnementRequis);

    // Rechercher un film par titre (insensible à la casse)
    List<Film> findByTitreContainingIgnoreCase(String titre);

    // Trouver les films par année
    List<Film> findByAnnee(int annee);
}