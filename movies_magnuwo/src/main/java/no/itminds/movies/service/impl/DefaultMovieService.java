package no.itminds.movies.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.service.MovieService;

@Service
public class DefaultMovieService implements MovieService {

	@Autowired
	private MovieRepository movieRepo;
	
	@Override
	public List<Movie> getAll() {
		// TODO Auto-generated method stub
		return movieRepo.findAll();
	}

	@Override
	public Movie getDetails(Long id) {
		// TODO Auto-generated method stub
		Movie movie = movieRepo.getOne(id);
		return movie;
	}

	@Override
	public void postComment(User existingUser, String title, String comment, Long movieId) {
		Optional<Movie> selectedMovieOpt = movieRepo.findById(movieId);
		if(selectedMovieOpt.isPresent()) {
			Movie selectedMovie = selectedMovieOpt.get();
			
			Comment newComment = new Comment(title, comment, existingUser);
			selectedMovie.addComment(newComment);
			
			movieRepo.saveAndFlush(selectedMovie);
			
		}
	}

	@Override
	public void vote(User author, long movieId, int rating) {
		Optional<Movie> selectedMovieOpt = movieRepo.findById(movieId);
		if(selectedMovieOpt.isPresent())
		{
			Movie selectedMovie = selectedMovieOpt.get();
			Optional<Rating> optRating = selectedMovie.getRatings().stream().filter(r -> r.getAuthor().equals(author)).findFirst();
			
			Rating newRating = new Rating(rating, author);
			if(optRating.isPresent()) {
				newRating = optRating.get();
				newRating.setRating(rating);
			}
			else
				selectedMovie.addRating(newRating);
			
			selectedMovie.calculateNewAverage();
			movieRepo.saveAndFlush(selectedMovie);
		}
	}

	@Override
	public Rating getCurrentRating(User currentUser, Movie currentMovie) {
		Optional<Rating> optRating = currentMovie.getRatings().stream().filter(r -> r.getAuthor().equals(currentUser)).findFirst();
		if(optRating.isPresent())
			return optRating.get();
		return null;
	}
	
	
}
