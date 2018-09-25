package no.itminds.movies.service;

import java.util.List;

import javax.persistence.PersistenceException;

import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.login.User;

public interface MovieService {
	
	public List<Movie> getAll();
	public Movie getDetails(Long id);
	public void postComment(User existingUser, String title, String comment, Long movieId) throws PersistenceException;
	public void vote(User existingUser, Long movieId, Integer rating) throws IllegalArgumentException, PersistenceException;
	public Rating getCurrentRating(User currentUser, Movie currentMovie);
	public Long save(Movie newMovie) throws PersistenceException;
}
