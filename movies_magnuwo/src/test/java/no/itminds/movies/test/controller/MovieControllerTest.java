package no.itminds.movies.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import no.itminds.movies.controller.MovieController;
import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.model.dto.CommentDTO;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.dto.VoteDTO;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.ActorRepository;
import no.itminds.movies.repository.GenreRepository;
import no.itminds.movies.service.MovieService;
import no.itminds.movies.service.UserService;

public class MovieControllerTest {

	private final static Logger LOGGER = LoggerFactory.getLogger(MovieControllerTest.class);

	@InjectMocks
	private MovieController movieController;
	@Mock
	private MovieService movieService;

	@Mock
	private UserService userService;

	@Mock
	private GenreRepository genreRepository;

	@Mock
	private ActorRepository actorRepository;

	@Mock
	private Validator validator;

	private List<Movie> testMovies;

	private User testUser;

	private Validator validatorImpl;

	private Authentication auth;
	
	private MovieDTO testDTOMovie;
	
	private final Date dateNow = Date.valueOf(LocalDate.now());

	@Before
	public void setup() throws NoSuchMethodException, SecurityException {
		MockitoAnnotations.initMocks(this);

		validatorImpl = Validation.buildDefaultValidatorFactory().getValidator();

		// Mocking authenication - makes sure it does not throw a nullpointer ex
		auth = Mockito.mock(Authentication.class);
		SecurityContext secContext = Mockito.mock(SecurityContext.class);

		when(secContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(secContext);

		// Build three movies
		MovieBuilder builder = new MovieBuilder();

		Movie movie1 = builder.id(1L).title("Pulp fiction").year("1999").plot("plot").build();

		Movie movie2 = builder.id(2L).title("Comming to America").year("1988").plot("plot2").build();

		Movie movie3 = builder.id(3L).title("Kill Bill").year("1960").plot("plot3").build();

		movie3.addComment(new Comment("Comment1", "Comment1Body", testUser));
		movie3.addComment(new Comment("Comment2", "Comment2Body", testUser));
		movie3.addComment(new Comment("Comment3", "Comment3Body", testUser));

		testMovies = Arrays.asList(movie1, movie2, movie3);

		// Test user
		testUser = new User("magne@hotmail.com", "secret", "Harry", "Hole");
		
		//Test DTO
		testDTOMovie = new MovieDTO("Test", "1945", "Something happens", 
				"Good", "46", "3.6", "testUrl", dateNow, dateNow, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}

	@Test
	public void testGetMovies() {
		// Assumes the DB contains 9 records - requesting pages with size 3.

		// Arrange
		Pageable pageable = new PageRequest(0, 3, Sort.Direction.DESC, "created");
		Page<Movie> firstPageMock = new PageImpl<>(testMovies, pageable, 9);

		// When
		when(movieService.getAll(pageable)).thenReturn(firstPageMock);

		Page<MovieDTO> firstPageActual = movieController.getMovies(pageable);
		Mockito.verify(movieService).getAll(pageable);
		// Then
		assertEquals("The number of Movies returned should be 3", testMovies.size(),
				firstPageActual.getNumberOfElements());
	}

	@Test
	public void testGetDetails() {
		// Arrange
		final Long movieId = new Long(1);
		Movie movieMock = new Movie(movieId);

		// When
		when(movieService.getDetails(movieId)).thenReturn(movieMock);
		Movie actualMovie = movieController.getDetails(movieId);
		Mockito.verify(movieService).getDetails(movieId);

		// Then
		assertEquals("", movieMock.getId(), actualMovie.getId());
	}

	@Test
	public void testAddMovie_validInput() throws Exception {
		// Arrange
		final MovieDTO EXPECTED_MOVIE_DTO = testDTOMovie;
		final Movie EXPECTED_MOVIE = new MovieBuilder().fromMovieDTO(EXPECTED_MOVIE_DTO).build();

		// When
		when(movieService.save(EXPECTED_MOVIE_DTO)).thenReturn(EXPECTED_MOVIE);
		ResponseEntity<?> responseEntity = movieController.addMovie(EXPECTED_MOVIE_DTO);

		// Then
		Mockito.verify(movieService, Mockito.times(1)).save(EXPECTED_MOVIE_DTO);

		assertEquals("Should return 201 CREATED", HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals("Body should be a movie", MovieDTO.class, responseEntity.getBody().getClass());

		MovieDTO actualMovie = (MovieDTO) responseEntity.getBody();
		assertEquals("Title should be correct", EXPECTED_MOVIE_DTO.getTitle(), actualMovie.getTitle());
		assertEquals("Plot should be correct", EXPECTED_MOVIE_DTO.getPlot(), actualMovie.getPlot());
		assertEquals("Created date should be correct", EXPECTED_MOVIE_DTO.getCreatedDate(), actualMovie.getCreatedDate());
		assertEquals("Year should be correct", EXPECTED_MOVIE_DTO.getYear(), actualMovie.getYear());
	}

	@Test
	public void testAddMovie_InvalidInput() throws Exception {
		// Arrange
		testDTOMovie.setPlot(null);
		testDTOMovie.setTitle(null);
		testDTOMovie.setCreatedDate(null);
		testDTOMovie.setYear(null);
		
		final MovieDTO EXPECTED_MOVIE_DTO = testDTOMovie;
		Movie EXPECTED_MOVIE = new MovieBuilder().fromMovieDTO(EXPECTED_MOVIE_DTO).build();

		// When
		when(validator.validate(EXPECTED_MOVIE_DTO)).thenReturn(validatorImpl.validate(EXPECTED_MOVIE_DTO));
		when(movieService.save(EXPECTED_MOVIE_DTO)).thenReturn(EXPECTED_MOVIE);
		ResponseEntity<?> responseEntity = movieController.addMovie(EXPECTED_MOVIE_DTO);

		// Then
		assertEquals("Should return bad request upon validation errors", HttpStatus.BAD_REQUEST,
				responseEntity.getStatusCode());
		assertEquals("Should return validation failed msg", String.class, responseEntity.getBody().getClass());
	}

	@Test
	public void testSubmitComment() {
		// Arrange
		final String TITLE = "TestTitle";
		final String COMMENT = "TestComment";
		final long MOVIE_ID = 5;

		final Comment COMMENT_EXPECTED = new Comment(TITLE, COMMENT, testUser);
		// When

		when(userService.findByEmail(Mockito.any())).thenReturn(testUser);
		when(movieService.postComment(testUser, TITLE, COMMENT, MOVIE_ID)).thenReturn(COMMENT_EXPECTED);

		ResponseEntity<?> responseEntity = movieController.submitComment(new CommentDTO(TITLE, COMMENT, MOVIE_ID));
		// Then
		Mockito.verify(userService).findByEmail(Mockito.any());
		assertEquals("Should return the persisted comment", responseEntity.getBody(), COMMENT_EXPECTED);
		assertEquals("Should return statuscode CREATED", HttpStatus.CREATED, responseEntity.getStatusCode());
	}

	@Test
	public void testSubmitComment_InvalidInput() {
		// Arrange
		final String TITLE = "TestTitle";
		final String COMMENT = "TestComment";
		final long MOVIE_ID = 5;
		// When
		when(userService.findByEmail(Mockito.any())).thenReturn(null);

		ResponseEntity<?> responseEntity = movieController.submitComment(new CommentDTO(TITLE, COMMENT, MOVIE_ID));
		// Then
		Mockito.verify(userService).findByEmail(Mockito.any());
		Mockito.verifyZeroInteractions(movieService);
		assertEquals("Should return unauthorized string", responseEntity.getBody(), "Unauthorized: not logged in");
		assertEquals("Should return statuscode Unauthorized", HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}

	@Test
	public void testVote_ValidInput() {
		// Arrange
		final Integer RATING = 5;
		final MovieDTO EXPECTED_MOVIE_DTO = testDTOMovie;
		final Movie EXPECTED_MOVIE = new MovieBuilder().
				id(new Long(4)).
				fromMovieDTO(EXPECTED_MOVIE_DTO).
				build();

		// When
		when(userService.findByEmail(Mockito.any())).thenReturn(testUser);
		when(movieService.vote(testUser, EXPECTED_MOVIE.getId(), RATING)).thenReturn(EXPECTED_MOVIE);
		ResponseEntity<?> responseEntity = movieController.vote(new VoteDTO(RATING, EXPECTED_MOVIE.getId()));
		// Then
		Mockito.verify(userService).findByEmail(Mockito.any());
		Mockito.verify(movieService, Mockito.times(1)).vote(testUser, EXPECTED_MOVIE.getId(), RATING);

		
		assertTrue("Should return updated Movie", EXPECTED_MOVIE_DTO.equals(responseEntity.getBody()));
		assertEquals("Should return HttpStatus OK", HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void testVote_InvalidInput() {
		// Arrange
		final Integer RATING = 5;
		final Movie EXPECTED_MOVIE = testMovies.get(0);

		// When
		when(userService.findByEmail(Mockito.any())).thenReturn(null);
		ResponseEntity<?> responseEntity = movieController.vote(new VoteDTO(RATING, EXPECTED_MOVIE.getId()));

		// Then
		Mockito.verify(userService).findByEmail(Mockito.any());
		Mockito.verifyZeroInteractions(movieService);

		assertEquals("Should return bad request string", responseEntity.getBody(), "Unauthorized: not logged in");
		assertEquals("Should return statuscode BAD REQUEST", HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}

	@Test
	public void testComments() {
		// Arrange
		final Movie EXPECTED_MOVIE = testMovies.get(2);
		final Long MOVIE_ID = EXPECTED_MOVIE.getId();
		final List<Comment> COMMENTS = EXPECTED_MOVIE.getComments();

		assertTrue("Should have 3 comments", COMMENTS.size() == 3);

		final Pageable PAGEABLE = new PageRequest(0, 10, Direction.DESC, "created");
		final Page<Comment> EXPECTED_COMMENT_PAGE = new PageImpl<>(COMMENTS, PAGEABLE, 20);
		// When

		when(movieService.getComments(MOVIE_ID, PAGEABLE)).thenReturn(EXPECTED_COMMENT_PAGE);
		final Page<CommentDTO> ACTUAL_COMMENT_PAGE = movieController.comments(MOVIE_ID, PAGEABLE);
		// Then

		Mockito.verify(movieService).getComments(MOVIE_ID, PAGEABLE);

		assertEquals("Should return 3 elements", EXPECTED_COMMENT_PAGE.getNumber(), ACTUAL_COMMENT_PAGE.getNumber());

	}

	@Test
	public void testGetGenres() {
		// Arrange
		List<Genre> EXPECTED_GENRES = Arrays.asList(new Genre("Action"), new Genre("Horror"), new Genre("Comedy"));
		final Pageable PAGEABLE = new PageRequest(0, 10, Direction.ASC, "name");
		final Page<Genre> EXPECTED_GENRE_PAGE = new PageImpl<>(EXPECTED_GENRES, PAGEABLE, 6);
		// When
		when(genreRepository.findAll(PAGEABLE)).thenReturn(EXPECTED_GENRE_PAGE);
		Page<Genre> ACTUAL_GENRE_PAGE = movieController.getGenres(PAGEABLE);

		// Then
		assertEquals("Should return a list of 3 Genres", EXPECTED_GENRE_PAGE.getContent().size(),
				ACTUAL_GENRE_PAGE.getContent().size());
		for(int i = 0; i < EXPECTED_GENRE_PAGE.getContent().size(); i++) {
			final Genre EXPECTED = EXPECTED_GENRE_PAGE.getContent().get(i);
			final Genre ACTUAL = ACTUAL_GENRE_PAGE.getContent().get(i);
			
			assertEquals("Should be equal genres, since there is not sorting...", EXPECTED,  ACTUAL); 
		}
	}

	@Test
	public void testGetActors() {
		// Arrange
		List<Actor> EXPECTED_ACTORS = Arrays.asList(new Actor("Samuel Jackson"), new Actor("Clint Eastwood"), new Actor("Uma Thurman"));
		final Pageable PAGEABLE = new PageRequest(0, 10, Direction.ASC, "name");
		final Page<Actor> EXPECTED_ACTOR_PAGE = new PageImpl<>(EXPECTED_ACTORS, PAGEABLE, 6);
		// When
		when(actorRepository.findAll(PAGEABLE)).thenReturn(EXPECTED_ACTOR_PAGE);
		Page<Actor> ACTUAL_ACTOR_PAGE = movieController.getActors(PAGEABLE);
		// Then
		assertEquals("Should return a list of 3 Actors", EXPECTED_ACTOR_PAGE.getContent().size(),
				ACTUAL_ACTOR_PAGE.getContent().size());
		
		for(int i = 0; i < EXPECTED_ACTOR_PAGE.getContent().size(); i++) {
			final Actor EXPECTED = EXPECTED_ACTOR_PAGE.getContent().get(i);
			final Actor ACTUAL = ACTUAL_ACTOR_PAGE.getContent().get(i);
			
			assertEquals("Should be equal actors, since there is not sorting...", EXPECTED,  ACTUAL); 
		}
	}
}
