package no.itminds.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.itminds.movies.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
