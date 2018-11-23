package no.itminds.movies.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.dto.CommentDTO;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.dto.VoteDTO;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.ActorRepository;
import no.itminds.movies.repository.GenreRepository;
import no.itminds.movies.service.MovieService;
import no.itminds.movies.service.UserService;
import no.itminds.movies.service.impl.TokenAuthenticationService;

/**
 * <h1>MovieController</h1>
 * <p>
 * A rest api for supporting operations on the Movie model, including comments
 * and ratings
 * </p>
 * 
 * @author Magnus WO
 *
 */

@RestController
@RequestMapping("api/")
public class MovieController {

	final static Logger logger = LoggerFactory.getLogger(MovieController.class);

	@Autowired
	private MovieService movieService;

	@Autowired
	private UserService userService;

	@Autowired
	private TokenAuthenticationService authenticationService;
	
	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private Validator validator;

	/**
	 * Controller endpoint for GETTING movies.
	 * 
	 * @param page  int which page to find, 0 indexed.
	 * @param value int defining
	 * @param size  int max pagesize
	 * @param sort  0 = Ascending, 1 = Descending
	 * @return {@link Page} containing a page of Movies.
	 */
	@GetMapping(value = "movies/getAll")
	public Page<MovieDTO> getMovies(@PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable) {
		Page<Movie> moviePage = movieService.getAll(pageable);
		return moviePage.map(movie -> new MovieDTO(movie));
	}

	/**
	 * Controller endpoints for GETTING details of a movie
	 * 
	 * @param id Id of Movie to se details
	 * @return Movie {@link Movie}
	 */
	@GetMapping(value = "movies/{id}")
	public Movie getDetails(@PathVariable Long id) {
		return movieService.getDetails(id);
	}

	/**
	 * Controller endpoint for GETTING actors.
	 * 
	 * @param page  int which page to find, 0 indexed.
	 * @param value int defining
	 * @param size  int max pagesize
	 * @param sort  0 = Ascending, 1 = Descending
	 * @return {@link Page} containing all actors
	 */
	@GetMapping(value = "actors/getAll")
	public Page<Actor> getActors(
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "name") Pageable pageable) {
		return actorRepository.findAll(pageable);
	}

	/**
	 * 
	 * @param movieId
	 * @param request
	 * @return {@link Rating} containing the given rating for the user
	 */
	@GetMapping(value="movies/rating") 
	public Rating getRating(@RequestParam String movieId, HttpServletRequest request) {
		User user = getCurrentUser(request);
		return movieService.getCurrentRating(user, Long.parseLong(movieId));
	}
	
	/**
	 * Controller endpoint for GETTING genres.
	 * 
	 * @param page  int which page to find, 0 indexed.
	 * @param value int defining
	 * @param size  int max pagesize
	 * @param sort  0 = Ascending, 1 = Descending
	 * @return {@link Page} containing all genres
	 */
	@GetMapping(value = "genres/getAll")
	public Page<Genre> getGenres(
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "name") Pageable pageable) {
		return genreRepository.findAll(pageable);
	}

	/**
	 * Controller endpoint for GETTING comments.
	 * 
	 * @param movieId
	 * @param page    int which page to find, 0 indexed.
	 * @param value   int defining
	 * @param size    int max pagesize
	 * @param sort    0 = Ascending, 1 = Descending
	 * @return {@link Page} containing all comments related to the given movieId
	 */
	@GetMapping(value = "movies/comments")
	public Page<CommentDTO> comments(@RequestParam Long movieId,
			@PageableDefault(page = 0, size = 10) Pageable pageable,
			HttpServletRequest request) {
		Page<Comment> comments = movieService.getComments(movieId, pageable);
		return comments.map(comment -> new CommentDTO(comment.getTitle(), comment.getComment(), movieId, comment.getCreated(), comment.getAuthor()));
	}

	/**
	 * Controller endpoint for POSTING a new movie
	 * 
	 * @param newMovieDTO {@link MovieDTO}
	 * @return {@link ResponseEntity} containing the saved Movie with an id and
	 *         status code {@link HttpStatus.CREATED}. Returns
	 *         {@link HttpStatus.BAD_REQUEST} in case of validation errors
	 */
	@PostMapping(value = "movies/addMovie")
	public ResponseEntity<?> addMovie(@RequestBody MovieDTO newMovieDTO) {
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

	/**
	 * Controller endpoint for POSTING a comment.
	 * 
	 * @param commentDTO {@link CommentDTO}
	 * @return {@link ResponseEntity} containing saved comment with an id, and
	 *         {@HttpStatus.CREATED} upon success. Returns {@link ResponseEntity}
	 *         with {@link HttpStatus.BAD_REQUEST} upon authorization error
	 *         {@link HttpStatus.UNAUTHORIZED}.
	 */
	@PostMapping(value = "movies/submitComment")
	public ResponseEntity<?> submitComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
		List<String> formErrors = MovieController.validate(commentDTO, validator);
		User existingUser = getCurrentUser(request);
		// If validation not approved - return
		if (formErrors.size() > 0) {
			// More spesific error message?
			return new ResponseEntity<>("Validation failed: " + formErrors, HttpStatus.BAD_REQUEST);
		}
		CommentDTO persistedComment = new CommentDTO(movieService.postComment(existingUser, commentDTO.getTitle(),
				commentDTO.getComment(), commentDTO.getMovieId()), commentDTO.getMovieId());
		return new ResponseEntity<>(persistedComment, HttpStatus.CREATED);

	}

	/**
	 * Controller endpoint for POSTING a vote.
	 * 
	 * @param voteDTO {@link VoteDTO}
	 * @return {@link ResponseEntity} containing the updated Movie with recalculated
	 *         average rating, and related list of ratings and {@HttpStatus.OK} upon
	 *         success. Returns {@link ResponseEntity}.
	 */
	@PostMapping(value = "movies/vote")
	public ResponseEntity<?> vote(@RequestBody VoteDTO voteDTO, HttpServletRequest request) {
		User existingUser = getCurrentUser(request);
		MovieDTO movieDTO = new MovieDTO(movieService.vote(existingUser, voteDTO.getMovieId(), voteDTO.getRating()));
		return new ResponseEntity<>(movieDTO, HttpStatus.OK);
	}

	/*
	 * Helper methods
	 */
	private User getCurrentUser(HttpServletRequest request) {
		Authentication auth = authenticationService.getAuthentication(request);
		if (auth != null)
			return userService.findByEmail(auth.getName());
		return null;
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
