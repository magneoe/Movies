package no.itminds.movies.service;

import java.util.List;

import javax.persistence.PersistenceException;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.login.User;

public interface MovieService {
	public Movie getDetails(Long id);

	public void postComment(User existingUser, Comment comment) throws PersistenceException;

	public void vote(User existingUser, Long movieId, Integer rating)
			throws IllegalArgumentException, PersistenceException;

	public Rating getCurrentRating(User currentUser, Movie currentMovie);

	public Long save(Movie newMovie) throws Exception;

	public List<Movie> getMovies();

	public List<Actor> getActors();

	public List<Genre> getGenres();
}
