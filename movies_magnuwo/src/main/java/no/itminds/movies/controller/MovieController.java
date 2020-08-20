package no.itminds.movies.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.dto.CommentDTO;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.login.User;
import no.itminds.movies.service.MovieService;
import no.itminds.movies.service.UserService;
import no.itminds.movies.util.CommentAdapter;
import no.itminds.movies.util.MovieAdapter;

@RestController
@RequestMapping("api/v1/movies/")
public class MovieController {

	final static Logger logger = LoggerFactory.getLogger(MovieController.class);

	@Autowired
	private MovieService movieService;

	@Autowired
	private UserService userService;
	
	@GetMapping("/actors")
	public List<Actor> getActors(){
		return movieService.getActors();
	}
	
	@GetMapping("/genres")
	public List<Genre> getGenres(){
		return movieService.getGenres();
	}

	@PostMapping(value = "create-movie")
	public ResponseEntity<String> submitNewMovie(@Valid MovieDTO newMovieDTO, BindingResult bindingResult) {
		List<Actor> actors = movieService.getActors();
		
		// If validation not approved - return
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>("Error in form: " + bindingResult.getFieldErrors(), HttpStatus.BAD_REQUEST);
		}
		// If validation approved - persist and return to details view
		try {
			Long id = movieService.save(MovieAdapter.fromMovieDTO(newMovieDTO, actors));
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug(ex.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(value = "details/{id}")
	public ResponseEntity<Movie> getDetails(@PathVariable Long id) {
		Movie existingMovie = movieService.getDetails(id);
		if(existingMovie == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		Rating currentRating = movieService.getCurrentRating(getCurrentUser(), existingMovie);
		existingMovie.setUserRating(currentRating);
		
		return new ResponseEntity<>(existingMovie, HttpStatus.OK);
	}

	@PostMapping(value = "submit-comment")
	public ResponseEntity<String> submitComment(@Valid CommentDTO commentDTO, BindingResult bindingResults) {

		User existingUser = getCurrentUser();

		if (existingUser != null) { 
			if(!bindingResults.hasErrors()) {
				movieService.postComment(existingUser, CommentAdapter.fromCommentDTO(commentDTO, existingUser, new Date()));
				return new ResponseEntity<String>(HttpStatus.CREATED);
			}
			else {
				logger.info("Unable to post new comment");
			}
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(value = "vote")
	public ResponseEntity<String> vote(Integer rating, Long movieId) {
		User existingUser = getCurrentUser();
		movieService.vote(existingUser, movieId, rating);

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	/*
	 * Helper methods
	 */
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.loadUserByEmail(auth.getName());
	}
}
