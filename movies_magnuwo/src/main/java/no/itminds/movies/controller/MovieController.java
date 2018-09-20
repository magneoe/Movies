package no.itminds.movies.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.login.User;
import no.itminds.movies.service.MovieService;
import no.itminds.movies.service.UserService;

@Controller
@RequestMapping("movies/")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private UserService userService;
	
	protected static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@RequestMapping(value = {"/*", "index"})
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.getModel().put("movies", movieService.getAll());
	
		User existingUser = getCurrentUser();
		if(existingUser != null) {
			modelAndView.addObject("username", existingUser.getName() + " " + existingUser.getLastname());
		}
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
	
	@RequestMapping(value="submitComment", method = RequestMethod.POST)
	public RedirectView submitComment(String title, String comment, long movieId) {
		
		User existingUser = getCurrentUser();
		
		if(existingUser != null) {
			movieService.postComment(existingUser, title, comment, movieId);
		}
		
		return new RedirectView("details/" + movieId);
	}
	
	@RequestMapping(value="vote", method=RequestMethod.POST)
	public RedirectView vote(int rating, long movieId) {
		User existingUser = getCurrentUser();
		movieService.vote(existingUser, movieId, rating);
		
		return new RedirectView("details/" + movieId);
	}
	
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.findByEmail(auth.getName());
	}
}
