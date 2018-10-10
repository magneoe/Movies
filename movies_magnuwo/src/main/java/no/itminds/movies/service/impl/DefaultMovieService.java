package no.itminds.movies.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.itminds.movies.exceptions.NotFoundException;
import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.CommentRepository;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.service.MovieService;

@Service
public class DefaultMovieService implements MovieService {

	private static Logger logger = LoggerFactory.getLogger(DefaultMovieService.class);

	@Autowired
	private MovieRepository movieRepo;

	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Movie> getAll() {
		return movieRepo.findAll();
	}

	@Override
	public Page<Movie> getAll(Pageable pageable) {
		return movieRepo.findAll(pageable);
	}

	@Override
	public Movie getDetails(Long id) {
		return movieRepo.findById(id).orElseThrow(() -> new NotFoundException("Movie with id " + id + " is not found",
				null, id, Movie.class.getSimpleName()));
	}

	@Override
	public Comment postComment(User existingUser, String title, String comment, Long movieId) {
		if (existingUser == null || title == null || comment == null || movieId == null)
			throw new IllegalArgumentException("Invalid parameters for creating a comment");

		Movie selectedMovie = movieRepo.findById(movieId)
				.orElseThrow(() -> new NotFoundException("Movie with id " + movieId + " was not found", null, movieId,
						Movie.class.getSimpleName()));
		Comment newComment = new Comment(title, comment, existingUser);
		newComment = commentRepo.save(newComment);
		
		selectedMovie.addComment(newComment);
		selectedMovie = movieRepo.saveAndFlush(selectedMovie);
		return newComment;
	}

	@Override
	public Movie vote(User author, Long movieId, Integer rating) {
		// Guard
		if (author == null || movieId == null || rating == null)
			throw new IllegalArgumentException("Vote: illegal argument(s)");

		Movie selectedMovie = movieRepo.findById(movieId)
				.orElseThrow(() -> new NotFoundException("Movie with id " + movieId + " is not found", null, movieId,
						Movie.class.getSimpleName()));

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

		return movieRepo.saveAndFlush(selectedMovie);
	}

	@Override
	public Rating getCurrentRating(User currentUser, Movie currentMovie) {
		if (currentUser == null || currentMovie == null)
			throw new IllegalArgumentException("Get current rating: invalid input");

		Optional<Rating> optRating = currentMovie.getRatings().stream().filter(r -> r.getAuthor().equals(currentUser))
				.findFirst();
		if (optRating.isPresent()) {
			return optRating.get();
		}
		return null;
	}
	@Override
	public Rating getCurrentRating(User currentUser, Long movieId) {
		if (currentUser == null || movieId == null)
			throw new IllegalArgumentException("Get current rating: invalid input");
		Movie currentMovie = getDetails(movieId);
		
		Optional<Rating> optRating = currentMovie.getRatings().stream().filter(r -> r.getAuthor().equals(currentUser))
				.findFirst();
		if (optRating.isPresent()) {
			return optRating.get(); 
		}
		return new Rating();
	}

	@Override
	@Transactional
	public Movie save(MovieDTO newMovieDTO) {
		if (newMovieDTO == null) {
			throw new IllegalArgumentException("Unable to save a new Movie being null");
		}
		MovieBuilder builder = new MovieBuilder();
		Movie newMovie = builder.fromMovieDTO(newMovieDTO).build();
		return entityManager.merge(newMovie);
	}

	@Override
	public Page<Comment> getComments(Long movieId, Pageable pageable) {
		if(movieId == null)
			throw new IllegalArgumentException("Movie id null");
		return movieRepo.getComments(movieId, pageable);
	}

	
}
