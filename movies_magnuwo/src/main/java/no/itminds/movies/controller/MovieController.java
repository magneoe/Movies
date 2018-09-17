package no.itminds.movies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import no.itminds.movies.service.MovieService;

@Controller
@RequestMapping("movies/")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@RequestMapping(value = "index")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.getModel().put("movies", movieService.getAll());
		return modelAndView;
	}

	@RequestMapping(value = "details/{id}", method = RequestMethod.GET)
	public ModelAndView getDetails(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("details");
		modelAndView.getModel().put("movie", movieService.getDetails(id));
		return modelAndView;
	}
}
