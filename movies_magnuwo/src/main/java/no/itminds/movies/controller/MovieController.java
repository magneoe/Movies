package no.itminds.movies.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.CacheObject;
import no.itminds.movies.model.Comment;
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

//	private final String CACHE_ENTRY_GENRES = "CAHCE_ENTRY_GENRES";
//	private final String CACHE_ENTRY_ACTORS = "CAHCE_ENTRY_ACTORS";
//	private static Map<String, CacheObject> cache = new HashMap<>();

	@GetMapping(value ="movies/getAll")
	public Page<Movie> getMovies(@PageableDefault(page=0, value=10) Pageable pageable) {
		return movieService.getAll(pageable);
	}
	@GetMapping(value = "movies/{id}")
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

	@GetMapping(value="movies/comments")
	public Page<Comment> comments(
			@RequestParam Long movieId, 
			@PageableDefault(page=0, size=10, sort={"created"}, direction=Direction.ASC) Pageable pageable) {
		return movieService.getComments(movieId, pageable);
	}

	@PostMapping(value="movies/addMovie")
	public ResponseEntity<?> addMovie(@RequestBody Movie newMovie) throws Exception  {
		List<String> formErrors = validateNewMovie(newMovie);
		
		// If validation not approved - return
		if (formErrors.size() > 0) {
			//More spesific error message?
			return new ResponseEntity<>("Validation failed: " + formErrors.get(0), HttpStatus.BAD_REQUEST);
		}
		// If validation approved - persist and return to details view
		newMovie = movieService.save(newMovie);
	
		return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
	}


	@PostMapping(value = "movies/submitComment")
	public ResponseEntity<?> submitComment(@RequestBody String title, @RequestBody String comment, @RequestBody long movieId) {

		User existingUser = getCurrentUser();

		if (existingUser != null) {
			Comment persistedComment = movieService.postComment(existingUser, title, comment, movieId);
			return new ResponseEntity<>(persistedComment, HttpStatus.CREATED);
		}
		return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
	}

	@PostMapping(value = "movies/vote")
	public ResponseEntity<?> vote(@RequestBody Integer rating, @RequestBody Long movieId) {
		User existingUser = getCurrentUser();
		Movie updatedMovie = movieService.vote(existingUser, movieId, rating);

		return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
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

	private List<String> validateNewMovie(Movie newMovie) {
		List<String> formErrors = new ArrayList<>();
		
		if(newMovie.getCreatedString().trim().length() > 0)
		{
			try {
				LocalDate.parse(newMovie.getCreatedString(), DateTimeFormatter.ofPattern(MovieBuilder.DATE_PATTERN));
			} catch (Exception ex) {
				formErrors.add("Illegal date formats");
			}
		}
		if(newMovie.getReleaseDateString().trim().length() > 0)
		{
			try {
				LocalDate.parse(newMovie.getReleaseDateString(), DateTimeFormatter.ofPattern(MovieBuilder.DATE_PATTERN));
			} catch (Exception ex) {
				formErrors.add("Illegal date formats");
			}
		}
		
		Set<ConstraintViolation<Movie>> violations = validator.validate(newMovie);
		//violations.stream().forEach(violation -> formErrors.add(violation.getMessage()));
		violations.stream().forEach(violation -> formErrors.add(violation.getMessage()));
		return formErrors;
	}
}
