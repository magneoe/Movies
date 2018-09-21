package no.itminds.movies.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {
	
	private static Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/")
	public RedirectView home() {
		logger.info("Homecontroller redirects to movies/index");
		return new RedirectView("movies/index");
	}
}
