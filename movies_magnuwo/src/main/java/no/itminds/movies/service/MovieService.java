package no.itminds.movies.service;

import java.util.List;

import no.itminds.movies.model.Movie;

public interface MovieService {
	
	public List<Movie> getAll();
	public Movie getDetails(Long id);
}
