package no.itminds.movies.service;

import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.login.User;

public interface MovieService {
	
	public List<Movie> getAll();
	public Page<Movie> getAll(Pageable pageable);
	public Movie getDetails(Long id);
	public Comment postComment(User existingUser, String title, String comment, Long movieId) throws PersistenceException;
	public Movie vote(User existingUser, Long movieId, Integer rating) throws IllegalArgumentException, PersistenceException;
	public Rating getCurrentRating(User currentUser, Movie currentMovie);
	public Movie save(MovieDTO newMovieDTO);
	public Page<Comment> getComments(Long movieId, Pageable pageable);
}
