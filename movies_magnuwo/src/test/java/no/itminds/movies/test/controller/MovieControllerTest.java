package no.itminds.movies.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.PageAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import no.itminds.movies.controller.MovieController;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.ActorRepository;
import no.itminds.movies.repository.GenreRepository;
import no.itminds.movies.service.MovieService;
import no.itminds.movies.service.UserService;

public class MovieControllerTest {

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
	

	private Validator validatorImpl;

	@Before
	public void setup() throws NoSuchMethodException, SecurityException {
		MockitoAnnotations.initMocks(this);
		
		validatorImpl = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	public void testGetMovies() {
		// Assumes the DB contains 9 records - requesting pages with size 3.

		// Arrange
		Pageable pageable = new PageRequest(0, 3, Sort.Direction.ASC, "created");
		List<Movie> moviesMocks = Arrays.asList(new Movie(new Long(1)), new Movie(new Long(2)), new Movie(new Long(3)));
		Page<Movie> firstPageMock = new PageImpl<>(moviesMocks, pageable, 9);

		// When
		when(movieService.getAll(pageable)).thenReturn(firstPageMock);

		Page<Movie> firstPageActual = movieController.getMovies(pageable);
		Mockito.verify(movieService).getAll(pageable);
		// Then
		assertEquals("The number of Movies returned should be 3", moviesMocks.size(),
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
		MovieBuilder builder = new MovieBuilder();
		builder.plot("Test plot");
		builder.created("04-10-1999");
		builder.title("Test title");
		builder.year("1988");

		Movie mockMovie = builder.build();
		
		// When
		when(movieService.save(mockMovie)).thenReturn(mockMovie);
		ResponseEntity<?> responseEntity = movieController.addMovie(mockMovie);

		// Then
		Mockito.verify(movieService).save(mockMovie);

		assertEquals("Should return 201 CREATED", HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals("Body should be a movie", Movie.class, responseEntity.getBody().getClass());

		Movie actualMovie = (Movie) responseEntity.getBody();
		assertEquals("Title should be correct", mockMovie.getTitle(), actualMovie.getTitle());
		assertEquals("Plot should be correct", mockMovie.getPlot(), actualMovie.getPlot());
		assertEquals("Created date should be correct", mockMovie.getCreated(), actualMovie.getCreated());
		assertEquals("Year should be correct", mockMovie.getYear(), actualMovie.getYear());
	}

	@Test
	public void testAddMovie_InvalidInput() throws Exception {
		// Arrange
		MovieBuilder builder = new MovieBuilder();
		builder.plot(null);
		builder.created("04-10-1999");
		builder.title("Test title");
		builder.year("1988");

		Movie mockMovie = builder.build();
		
		// When
		when(validator.validate(mockMovie)).thenReturn(validatorImpl.validate(mockMovie));
		when(movieService.save(mockMovie)).thenReturn(mockMovie);
		ResponseEntity<?> responseEntity = movieController.addMovie(mockMovie);

		// Then
		assertEquals("Should return bad request upon validation errors", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Should return validation failed msg", String.class, responseEntity.getBody().getClass());
	}

	@Test
	public void testSubmitComment() {
		
	}
	
	@Test
	public void testVote() {
		
	}
	@Test
	public void testComments() {
		
	}
	@Test
	public void testGetGenres() {
		
	}
	@Test
	public void testGetActors() {
		
	}
}
