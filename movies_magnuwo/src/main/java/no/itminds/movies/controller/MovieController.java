package no.itminds.movies.controller;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.ActorRepository;
import no.itminds.movies.repository.GenreRepository;
import no.itminds.movies.service.MovieService;
import no.itminds.movies.service.UserService;

@Controller
@RequestMapping("movies/")
public class MovieController {

	final static Logger logger = LoggerFactory.getLogger(MovieController.class);

	@Autowired
	private MovieService movieService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private Validator validator;

	@RequestMapping(value = { "/*", "index" })
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.getModel().put("movies", movieService.getAll());

		User existingUser = getCurrentUser();
		if (existingUser != null) {
			modelAndView.addObject("user", existingUser);
		}
		return modelAndView;
	}

	@RequestMapping(value = "createMovie", method = RequestMethod.GET)
	public ModelAndView createMovie() {
		List<Actor> actors = actorRepository.findAll();
		Collections.sort(actors);

		List<Genre> genres = genreRepository.findAll();
		Collections.sort(genres);

		ModelAndView modelAndView = new ModelAndView("createMovie");
		modelAndView.addObject("actors", actors);
		modelAndView.addObject("genres", genres);

		return modelAndView;
	}

	@RequestMapping(value = "newMovie", method = RequestMethod.POST)
	public ModelAndView submitNewMovie(Movie newMovie) {
		ModelAndView modelAndView = createMovie();
		List<Object> formErrors = validateNewMovie(newMovie);
		// If validation not approved - return
		if (formErrors.size() > 0) {
			modelAndView.addObject("formErrors", formErrors);
			modelAndView.addObject("newMovie", newMovie);
			return modelAndView;
		}
		// If validation approved - persist and return to details view
		Long id = movieService.save(newMovie);
		modelAndView.setView(new RedirectView("details/" + id));

		return modelAndView;
	}

	@RequestMapping(value = "details/{id}", method = RequestMethod.GET)
	public ModelAndView getDetails(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("details");
		Movie currentMovie = movieService.getDetails(id);
		Rating currentRating = movieService.getCurrentRating(getCurrentUser(), currentMovie);

		modelAndView.getModel().put("movie", currentMovie);
		modelAndView.getModel().put("currentRating", currentRating);
		return modelAndView;
	}

	@RequestMapping(value = "submitComment", method = RequestMethod.POST)
	public RedirectView submitComment(String title, String comment, long movieId) {

		User existingUser = getCurrentUser();

		if (existingUser != null) {
			movieService.postComment(existingUser, title, comment, movieId);
		}

		return new RedirectView("details/" + movieId);
	}

	@RequestMapping(value = "vote", method = RequestMethod.POST)
	public RedirectView vote(int rating, long movieId) {
		User existingUser = getCurrentUser();
		movieService.vote(existingUser, movieId, rating);

		return new RedirectView("details/" + movieId);
	}

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.findByEmail(auth.getName());
	}

	private List<Object> validateNewMovie(Movie newMovie) {
		List<Object> formErrors = new ArrayList<>();
		// Validate dates
		try {
			newMovie.setCreated(newMovie.getCreatedDTOString());
			newMovie.setReleaseDate(newMovie.getReleaseDateDTO());
		} catch (Exception ex) {
			formErrors.add("Illegal date formats");
		}
		Set<ConstraintViolation<Movie>> violations = validator.validate(newMovie);
		List<Object> violationList = Arrays
				.asList(violations.stream().map(violation -> violation.getMessage()).toArray());
		formErrors.addAll(violationList);
		
		return formErrors;
	}

	// Error handling
	@ExceptionHandler(value = { DataAccessException.class, SQLDataException.class })
	protected ModelAndView handleDBExceptions(HttpServletRequest req, Exception ex) {
		final String errorMessage = "Database error: Could not complete the requested operation!";
		logger.debug(errorMessage);

		ModelAndView modelAndView = new ModelAndView(req.getRequestURI());
		modelAndView.getModel().put("errorMessage", errorMessage);
		return modelAndView;
	}

	@ExceptionHandler(value = Exception.class)
	protected ModelAndView handelExceptions(HttpServletRequest req, Exception ex) {
		ModelAndView modelAndView = new ModelAndView(req.getRequestURI());
		final String errorMessage = "Database error: Could not complete the requested operation!";
		logger.debug(errorMessage);

		modelAndView.getModel().put("errorMessage", errorMessage);
		return modelAndView;
	}
}
