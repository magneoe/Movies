package no.itminds.movies.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.dto.CommentDTO;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.dto.VoteDTO;
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

	@GetMapping(value = "movies/getAll")
	public Page<MovieDTO> getMovies(@PageableDefault(page = 0, value = 10) Pageable pageable) {
		Page<Movie> moviePage = movieService.getAll(pageable);
		return moviePage.map(movie -> new MovieDTO(movie));
	}

	@GetMapping(value = "movies/{id}")
	public Movie getDetails(@PathVariable Long id) {
		return movieService.getDetails(id);
	}

	@GetMapping(value = "actors/getAll")
	public Page<Actor> getActors(
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "name") Pageable pageable) {
		return actorRepository.findAll(pageable);
	}

	@GetMapping(value = "genres/getAll")
	public Page<Genre> getGenres(
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "name") Pageable pageable) {
		return genreRepository.findAll(pageable);
	}

	@GetMapping(value = "movies/comments")
	public Page<Comment> comments(@RequestParam Long movieId,
			@PageableDefault(page = 0, size = 10, sort = { "created" }, direction = Direction.ASC) Pageable pageable) {
		return movieService.getComments(movieId, pageable);
	}

	@PostMapping(value = "movies/addMovie")
	public ResponseEntity<?> addMovie(@RequestBody MovieDTO newMovieDTO) throws Exception {
		List<String> formErrors = MovieController.validate(newMovieDTO, validator);

		// If validation not approved - return
		if (formErrors.size() > 0) {
			// More spesific error message?
			return new ResponseEntity<>("Validation failed: " + formErrors, HttpStatus.BAD_REQUEST);
		}
		// If validation approved - persist and return to details view
		newMovieDTO = new MovieDTO(movieService.save(newMovieDTO));

		return new ResponseEntity<>(newMovieDTO, HttpStatus.CREATED);
	}

	@PostMapping(value = "movies/submitComment")
	public ResponseEntity<?> submitComment(@RequestBody CommentDTO commentDTO) {
		List<String> formErrors = MovieController.validate(commentDTO, validator);
		User existingUser = getCurrentUser();
		// If validation not approved - return
		if (formErrors.size() > 0) {
			// More spesific error message?
			return new ResponseEntity<>("Validation failed: " + formErrors, HttpStatus.BAD_REQUEST);
		}
		if(existingUser == null)
			return new ResponseEntity<>("Unauthorized: not logged in", HttpStatus.UNAUTHORIZED);

		Comment persistedComment = movieService.postComment(existingUser, commentDTO.getTitle(),
				commentDTO.getComment(), commentDTO.getMovieId());
		return new ResponseEntity<>(persistedComment, HttpStatus.CREATED);

	}

	@PostMapping(value = "movies/vote")
	public ResponseEntity<?> vote(@RequestBody VoteDTO voteDTO) {
		User existingUser = getCurrentUser();
		if (existingUser != null) {
			MovieDTO movieDTO = new MovieDTO(
					movieService.vote(existingUser, voteDTO.getMovieId(), voteDTO.getRating()));
			return new ResponseEntity<>(movieDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>("Unauthorized: not logged in", HttpStatus.UNAUTHORIZED);
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
	
	public static <T> List<String> validate(T objToValidate, Validator validator) {
		List<String> formErrors = new ArrayList<>();

		Set<ConstraintViolation<T>> violations = validator.validate(objToValidate);
		// violations.stream().forEach(violation ->
		// formErrors.add(violation.getMessage()));
		violations.stream().forEach(violation -> formErrors.add(violation.getMessage()));
		return formErrors;
	}
}
