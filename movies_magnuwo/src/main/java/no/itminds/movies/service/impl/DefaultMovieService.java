package no.itminds.movies.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.itminds.movies.exceptions.NotFoundException;
import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.dto.CommentDTO;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.ActorRepository;
import no.itminds.movies.repository.GenreRepository;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.service.MovieService;
import no.itminds.movies.util.CacheManager;

@Service
public class DefaultMovieService implements MovieService {

	private static Logger logger = LoggerFactory.getLogger(DefaultMovieService.class);

	@Autowired
	private MovieRepository movieRepo;
	
	@Autowired
	private GenreRepository genreRepo;
	
	@Autowired
	private ActorRepository actorRepo;

	@Autowired
	private EntityManager entityManager;


	@Override
	public Movie getDetails(Long id) {
		return movieRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Movie with id " + id + " is not found", null, id));
	}

	@Override
	public void postComment(User existingUser, Comment newComment) {
		if(existingUser == null)
			throw new PersistenceException("Unable to post a comment without an author");
		
		Movie selectedMovie = movieRepo.findById(newComment.getId())
				.orElseThrow(() -> new NotFoundException("Movie with id " + newComment.getId() + " was not found", null, newComment.getId()));
		selectedMovie.addComment(newComment);

		movieRepo.saveAndFlush(selectedMovie);
	}

	@Override
	public void vote(User author, Long movieId, Integer rating) throws PersistenceException, IllegalArgumentException {
		// Guard
		if (author == null || movieId == null || rating == null)
			throw new IllegalArgumentException("Vote: illegal argument(s)");

		try {
			Movie selectedMovie = movieRepo.findById(movieId).orElseThrow(
					() -> new NotFoundException("Movie with id " + movieId + " is not found", null, movieId));
			;

			Optional<Rating> optRating = selectedMovie.getRatings().stream().filter(r -> r.getAuthor().equals(author))
					.findFirst();

			// Checks if the user has rated this movie before, if yes overwrite the old
			Rating newRating = new Rating(rating, author);
			if (optRating.isPresent()) {
				newRating = optRating.get();
				newRating.setRating(rating);
			} else
				selectedMovie.addRating(newRating);

			Double newAverage = Movie.calculateNewAverage(selectedMovie.getRatings());
			selectedMovie.setAverageRating(newAverage);

			movieRepo.saveAndFlush(selectedMovie);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug(ex.getMessage());
			throw new PersistenceException("Error: Could not register the vote");
		}
	}

	@Override
	public Rating getCurrentRating(User currentUser, Movie currentMovie) {
		try {
			Optional<Rating> optRating = currentMovie.getRatings().stream()
					.filter(r -> r.getAuthor().equals(currentUser)).findFirst();
			if (optRating.isPresent()) {
				return optRating.get();
			}
		} catch (NullPointerException npex) {
			logger.debug(npex.getMessage());
		}
		return null;
	}

	@Override
	@Transactional
	public Long save(Movie newMovie) throws Exception {
		if (newMovie == null) {
			logger.debug("Unable to save a newMovie. Reason: null");
			throw new PersistenceException("Unable to save a newMovie");
		}

		newMovie = entityManager.merge(newMovie);
		return newMovie.getId();
	}
	
	@Override
	public List<Movie> getMovies() {
		List<Movie> movies = movieRepo.findAll();
		CacheManager.setMovies(movies);
		return movies;
	}

	@Override
	public List<Actor> getActors() {
		List<Actor> actors = actorRepo.findAll();
		CacheManager.setActors(actors);
		return actors;
	}

	@Override
	public List<Genre> getGenres() {
		List<Genre> genres = genreRepo.findAll();
		CacheManager.setGenres(genres);
		return genres;
	}
}
