package no.itminds.movies.test.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import no.itminds.movies.controller.MovieController;
import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Genre;
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
	public void setup() {
		MockitoAnnotations.initMocks(this);

		validatorImpl = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	public void testGetAll() {
		// Arrange
		List<Movie> moviesMocks = Arrays.asList(new Movie(), new Movie(), new Movie());
		when(movieService.getAll()).thenReturn(moviesMocks);

		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("magneoe@gmail.com");
		when(userService.findByEmail(any())).thenReturn(new User("magneoe@gmail.com", "secret", "Magnus", "Østeng"));

		// Test
//		ModelAndView result = movieController.index();
//		Map<String, Object> model = result.getModel();
//		verify(movieService).getAll();
//		verify(userService).findByEmail(any());
//
//		assertTrue(model.containsKey("movies"));
//
//		Object moviesObj = model.get("movies");
//		if (!(moviesObj instanceof List)) {
//			fail("The entry movies in the model should be a list");
//		}
//		List<Movie> moviesActual = (List) moviesObj;
//		assertEquals(moviesMocks.size(), moviesActual.size());

	}

	@Test
	public void testGetDetails() {
//		Long movieId = new Long(1);
//		// Arrange
//		Movie movieMock = new Movie(movieId);
//		when(movieService.getDetails(movieId)).thenReturn(movieMock);
//
//		// Test
//		ModelAndView modelAndView = movieController.getDetails(movieId);
//		Map<String, Object> model = modelAndView.getModel();
//
////		verify(movieService).getDetails(movieId);
//
//		assertTrue(model.containsKey("movie"));
//
//		Object movieObj = model.get("movie");
//		if (!(movieObj instanceof Movie)) {
//			fail("The entry movie in the model should be a Movie object");
//		}
//		Movie actualMovie = (Movie) movieObj;
//		assertThat(movieId, is(actualMovie.getId()));
	}

	@Test
	public void testValidateNewMovie_validInput() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// Arrange
		MovieDTO mockMovieDTO = new MovieDTO();
		mockMovieDTO.setReleaseDate("10-05-1999");
		mockMovieDTO.setCreatedDate("15-10-1998");
		mockMovieDTO.setPlot("Plot");
		mockMovieDTO.setTitle("Title");
		mockMovieDTO.setYear("1999");

		// When
		Method validateNewMovie = MovieController.class.getDeclaredMethod("validateNewMovie", new Class[] {MovieDTO.class});
		validateNewMovie.setAccessible(true);
		Object formErrorsObj = validateNewMovie.invoke(movieController, mockMovieDTO);

		// Then
		assertTrue(formErrorsObj instanceof List<?>);
		
		List<String> formErrors = (List) formErrorsObj;
		assertTrue(formErrors.size() == 0);
	}
	@Test
	public void testValidateNewMovie_InvalidInput() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// Arrange
		MovieDTO mockMovieDTO = new MovieDTO();
		mockMovieDTO.setReleaseDate("45-10-1999");
		mockMovieDTO.setCreatedDate(null);
		mockMovieDTO.setPlot(null);
		mockMovieDTO.setTitle(null);
		mockMovieDTO.setYear(null);

		// When
		when(validator.validate(mockMovieDTO)).thenReturn(validatorImpl.validate(mockMovieDTO));
		
		Method validateNewMovie = MovieController.class.getDeclaredMethod("validateNewMovie", new Class[] {MovieDTO.class});
		validateNewMovie.setAccessible(true);
		Object formErrorsObj = validateNewMovie.invoke(movieController, mockMovieDTO);

		// Then
		assertTrue(formErrorsObj instanceof List<?>);
		
		List<String> formErrors = (List) formErrorsObj;
		assertEquals("Number of errors reported in the validation should be 4", 4, formErrors.size());
	}

	@Test
	public void testCreateMovie() {
//		// Arrange
//
//		// When
//		ModelAndView modelAndView = movieController.createMovie();
//		Map<String, Object> model = modelAndView.getModel();
//		// Then
//		assertTrue(model.containsKey("newMovie"));
//
//		List<Actor> actors = (List<Actor>) model.get("actors");
//		assertNotNull(actors);
//
//		List<Genre> genres = (List<Genre>) model.get("genres");
//		assertNotNull(genres);
//
//		assertThat(modelAndView.getViewName(), Is.is("createMovie"));
	}

	@Test
	public void testSubmitNewMovie_validInput() throws Exception {

//		// Arrange
//
//		List<Actor> actorList = Arrays.asList(new Actor("Samuel Jackson"), new Actor("Uma Thurman"));
//
//		MovieDTO movieDTO = new MovieDTO();
//		movieDTO.setActors(new String[] { actorList.get(0).getName(), actorList.get(1).getName() });
//		movieDTO.setCreatedDate("05-08-2010");
//		movieDTO.setPlot("Plot");
//		movieDTO.setYear("1999");
//		movieDTO.setReleaseDate("10-06-2010");
//		movieDTO.setTitle("Title");
//
//		MovieBuilder builder = new MovieBuilder();
//		builder.fromMovieDTO(movieDTO, actorList);
//
//		// When
//		Long expectedId = new Long(2);
//		when(movieService.save(builder.build())).thenReturn(expectedId);
//		ModelAndView modelAndView = movieController.submitNewMovie(movieDTO);
//
//		/*
//		 * Test 1 - success scenario
//		 */
//		// Then
//		Map<String, Object> model = modelAndView.getModel();
//		assertNull(model.get("actors"));
//		assertNull(model.get("genres"));
//		assertNull(model.get("formErrors"));
//		assertNull(model.get("newMovie"));
//		assertTrue("The view should be a redirect view upon success", modelAndView.getView() instanceof RedirectView);

	}

	@Test
	public void testSubmitNewMovie_InvalidInput() {

//		// Arrange
//		MovieDTO movieDTO = new MovieDTO();
//		movieDTO.setCreatedDate("2017-10-16"); // Wrong format
//		movieDTO.setReleaseDate(null);
//		movieDTO.setPlot(null);
//		movieDTO.setYear(null);
//		movieDTO.setTitle(null);
//		
//		// When
//		Mockito.when(validator.validate(movieDTO)).thenReturn(validatorImpl.validate(movieDTO));
//		ModelAndView modelAndView = movieController.submitNewMovie(movieDTO);
//		
//		Map<String, Object> model = modelAndView.getModel();
//		Object formErrorsObj = model.get("formErrors");
//		//Then
//		assertTrue(formErrorsObj instanceof List);
//		assertNotNull(model.get("actors"));
//		assertNotNull(model.get("newMovie"));
//		assertNotNull(model.get("genres"));
//		
//		List<?> formErrors = (List<?>) formErrorsObj;
//		assertEquals("Number of errors reported should be 4", 4, formErrors.size());
//		assertThat(modelAndView.getViewName(), Is.is("createMovie"));
	}
}
