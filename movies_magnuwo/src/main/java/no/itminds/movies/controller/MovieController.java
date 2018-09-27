package no.itminds.movies.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.CacheObject;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.ActorRepository;
import no.itminds.movies.repository.GenreRepository;
import no.itminds.movies.service.MovieService;
import no.itminds.movies.service.UserService;

@RestController
@RequestMapping("api/")
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

	private final String CACHE_ENTRY_GENRES = "CAHCE_ENTRY_GENRES";
	private final String CACHE_ENTRY_ACTORS = "CAHCE_ENTRY_ACTORS";
//	private static Map<String, CacheObject> cache = new HashMap<>();

	@GetMapping(value ="movies/getAll", produces=MediaType.APPLICATION_JSON_VALUE)
	public Page<Movie> getMovies(@PageableDefault(page=0, value=10) Pageable pageable) {
		Page<Movie> page = movieService.getAll(pageable);
		
		return page;
	}
	@GetMapping(value = "movies/details/{id}")
	public Movie getDetails(@PathVariable Long id) {
		return movieService.getDetails(id);
	}
	
	@GetMapping(value = "actors/getAll")
	public Page<Actor> getActors(@PageableDefault(page=0, size=10) Pageable pageable) {
		return actorRepository.findAll(pageable);
	}
	@GetMapping(value = "genres/getAll")
	public Page<Genre> getGenres(@PageableDefault(page=0, size=10) Pageable pageable) {
		return genreRepository.findAll(pageable);
	}
	

//	@RequestMapping(value = "newMovie", method = RequestMethod.POST)
//	public ModelAndView submitNewMovie(MovieDTO newMovieDTO) {
//		
//		ModelAndView modelAndView = new ModelAndView("createMovie");
//		List<Actor> actors = getCachedItems(CACHE_ENTRY_ACTORS, actorRepository);
//		List<Object> formErrors = validateNewMovie(newMovieDTO);
//		
//		// If validation not approved - return
//		if (formErrors.size() > 0) {
//			modelAndView.addObject("actors", actors);
//			modelAndView.addObject("genres", getCachedItems(CACHE_ENTRY_GENRES, genreRepository));
//			modelAndView.addObject("formErrors", formErrors);
//			modelAndView.addObject("newMovie", newMovieDTO);
//			return modelAndView;
//		}
//		// If validation approved - persist and return to details view
//		MovieBuilder builder = new MovieBuilder();
//		builder.fromMovieDTO(newMovieDTO, actors);
//		try {
//			Long id = movieService.save(builder.build());
//			modelAndView.setView(new RedirectView("details/" + id));
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			logger.debug(ex.getMessage());
//			modelAndView.addObject("error", "Unable to Create new movie");
//		}
//		return modelAndView;
//	}

	

	@RequestMapping(value = "submitComment", method = RequestMethod.POST)
	public RedirectView submitComment(String title, String comment, long movieId) {

		User existingUser = getCurrentUser();

		if (existingUser != null) {
			movieService.postComment(existingUser, title, comment, movieId);
		}

		return new RedirectView("details/" + movieId);
	}

	@RequestMapping(value = "vote", method = RequestMethod.POST)
	public RedirectView vote(Integer rating, Long movieId) {
		User existingUser = getCurrentUser();
		movieService.vote(existingUser, movieId, rating);

		return new RedirectView("details/" + movieId);
	}

	/*
	 * Helper methods
	 */
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.findByEmail(auth.getName());
	}

//	private <T extends Comparable<T>> List<T> getCachedItems(final String CACHE_KEY,
//			JpaRepository<T, Long> repository, Pageable pageable) {
//		CacheObject<T> cacheObj = MovieController.cache.get(CACHE_KEY);
//
//		if (cacheObj == null || LocalDateTime.now().isAfter(cacheObj.getLastUpdated().plusMinutes(5))) {
//			Page<T> cachedItems = repository.findAll(pageable);
//			cache.put(CACHE_KEY, new CacheObject<>(CACHE_KEY, cachedItems));
//			logger.info("Cached objs are being loaded again");
//			return cachedItems;
//		}
//		
//		logger.info("Cached obj is being served from cache");
//		return cacheObj.getCacheItems();
//	}

	private List<Object> validateNewMovie(MovieDTO newMovieDTO) {
		List<Object> formErrors = new ArrayList<>();
		// Validate dates
		try {
			LocalDate.parse(newMovieDTO.getCreatedDate(), DateTimeFormatter.ofPattern(MovieBuilder.DATE_PATTERN));
			LocalDate.parse(newMovieDTO.getReleaseDate(), DateTimeFormatter.ofPattern(MovieBuilder.DATE_PATTERN));
		} catch (Exception ex) {
			formErrors.add("Illegal date formats");
		}
		Set<ConstraintViolation<MovieDTO>> violations = validator.validate(newMovieDTO);
		List<Object> violationList = Arrays
				.asList(violations.stream().map(violation -> violation.getMessage()).toArray());
		formErrors.addAll(violationList);

		return formErrors;
	}
}
