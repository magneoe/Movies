package no.itminds.movies.service;

import java.util.List;

import no.itminds.movies.errorhandling.MovieException;
import no.itminds.movies.model.Movie;

public interface MovieService {
	
	public List<Movie> getAll() throws MovieException;
	public Movie getDetails(Long id) throws MovieException;
}
