package no.itminds.movies.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnTransformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.service.MovieService;

@Service
public class DefaultMovieService implements MovieService {

	private static Logger logger = LoggerFactory.getLogger(DefaultMovieService.class);

	@Autowired
	private MovieRepository movieRepo;
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Movie> getAll() {
		return movieRepo.findAll();
	}

	@Override
	public Movie getDetails(Long id) {
		Movie movie = movieRepo.getOne(id);
		return movie;
	}

	@Override
	public void postComment(User existingUser, String title, String comment, Long movieId)
			throws PersistenceException, IllegalArgumentException {
		Optional<Movie> selectedMovieOpt = movieRepo.findById(movieId);
		if (selectedMovieOpt.isPresent()) {
			Movie selectedMovie = selectedMovieOpt.get();

			Comment newComment = new Comment(title, comment, existingUser);
			selectedMovie.addComment(newComment);

			movieRepo.saveAndFlush(selectedMovie);
		} else
			throw new PersistenceException("PersistenceException: The movie does not exist");
	}

	@Override
	public void vote(User author, Long movieId, Integer rating) throws PersistenceException, IllegalArgumentException {
		// Guard
		if (author == null || movieId == null || rating == null)
			throw new IllegalArgumentException("Vote: illegal argument(s)");

		try {
			Optional<Movie> selectedMovieOpt = movieRepo.findById(movieId);
			if (selectedMovieOpt.isPresent()) {
				Movie selectedMovie = selectedMovieOpt.get();
				Optional<Rating> optRating = selectedMovie.getRatings().stream()
						.filter(r -> r.getAuthor().equals(author)).findFirst();

				//Checks if the user has rated this movie before, if yes overwrite the old
				Rating newRating = new Rating(rating, author);
				if (optRating.isPresent()) {
					newRating = optRating.get();
					newRating.setRating(rating);
				} else
					selectedMovie.addRating(newRating);

				Double newAverage = Movie.calculateNewAverage(selectedMovie.getRatings());
				selectedMovie.setAverageRating(newAverage);
				
				movieRepo.saveAndFlush(selectedMovie);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug(ex.getMessage());
			throw new PersistenceException("Error: Could not register the vote");
		}
	}

	@Override
	public Rating getCurrentRating(User currentUser, Movie currentMovie) {
		try {
		Optional<Rating> optRating = currentMovie.getRatings().stream().filter(r -> r.getAuthor().equals(currentUser))
				.findFirst();
		if (optRating.isPresent())
			return optRating.get();
		}
		catch(Exception ex) {
			logger.debug(ex.getMessage());
		}
		return null;
	}

	@Override
	@Transactional
	public Long save(Movie newMovie) throws PersistenceException {
		if (newMovie == null) {
			logger.debug("Unable to save a newMovie. Reason: null");
			throw new PersistenceException("Unable to save a newMovie");
		}
		
		newMovie = entityManager.merge(newMovie);
		return newMovie.getId();
	}
}
