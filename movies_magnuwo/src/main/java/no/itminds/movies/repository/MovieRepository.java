package no.itminds.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.itminds.movies.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	
}
