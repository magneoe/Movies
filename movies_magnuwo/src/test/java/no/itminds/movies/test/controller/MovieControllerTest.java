package no.itminds.movies.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Validator;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
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

	private List<Movie> testMovies;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		// Build three movies
		MovieBuilder builder = new MovieBuilder();

		Movie movie1 = builder.title("Pulp fiction").year("1999").plot("plot").build();

		Movie movie2 = builder.title("Comming to America").year("1988").plot("plot2").build();

		Movie movie3 = builder.title("Kill Bill").year("1960").plot("plot3").build();

		testMovies = Arrays.asList(movie1, movie2, movie3);
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
		ModelAndView result = movieController.index();
		Map<String, Object> model = result.getModel();
		verify(movieService).getAll();
		verify(userService).findByEmail(any());

		assertTrue(model.containsKey("movies"));

		Object moviesObj = model.get("movies");
		if (!(moviesObj instanceof List)) {
			fail("The entry movies in the model should be a list");
		}
		List<Movie> moviesActual = (List) moviesObj;
		assertEquals(moviesMocks.size(), moviesActual.size());

	}

	@Test
	public void testGetDetails() {
		Long movieId = new Long(1);
		// Arrange
		Movie movieMock = new Movie(movieId);
		when(movieService.getDetails(movieId)).thenReturn(movieMock);

		// Test
		ModelAndView modelAndView = movieController.getDetails(movieId);
		Map<String, Object> model = modelAndView.getModel();

//		verify(movieService).getDetails(movieId);

		assertTrue(model.containsKey("movie"));

		Object movieObj = model.get("movie");
		if (!(movieObj instanceof Movie)) {
			fail("The entry movie in the model should be a Movie object");
		}
		Movie actualMovie = (Movie) movieObj;
		assertThat(movieId, is(actualMovie.getId()));
	}

	@Test
	public void testValidateNewMovie() {
		// Arrange
		MovieDTO mockMovieDTO = new MovieDTO();

		// When
		List<Object> formErrors = movieController.validateNewMovie(mockMovieDTO);

		// Then
		
	}

	@Test
	public void testCreateMovie() {
		//Arrange
		
		//When
		ModelAndView modelAndView = movieController.createMovie();
		Map<String, Object> model = modelAndView.getModel();
		//Then
		assertTrue(model.containsKey("newMovie"));
		
		List<Actor> actors = (List<Actor>) model.get("actors");
		assertNotNull(actors);
		
		List<Genre> genres = (List<Genre>) model.get("genres");
		assertNotNull(genres);
		
		assertThat(modelAndView.getViewName(), Is.is("createMovie"));
	}

	@Test
	public void testSubmitNewMovie() throws Exception {
		
		//Arrange
		
		List<Actor> actorList = Arrays.asList(new Actor("Samuel Jackson"), new Actor("Uma Thurman"));
		
		MovieDTO movieDTO = new MovieDTO();
		movieDTO.setActors(new String[] {actorList.get(0).getName(), actorList.get(1).getName()});
		movieDTO.setCreatedDate("05-08-2010");
		movieDTO.setPlot("Plot");
		movieDTO.setYear("1999");
		movieDTO.setReleaseDate("10-06-2010");
		movieDTO.setTitle("Title");
		
		MovieBuilder builder = new MovieBuilder();
		builder.fromMovieDTO(movieDTO, actorList);
		
		//When
		Long expectedId = new Long(2);
		when(movieService.save(builder.build())).thenReturn(expectedId);
		ModelAndView modelAndView = movieController.submitNewMovie(movieDTO);
		
		
		//Then
		Map<String, Object> model = modelAndView.getModel();
		assertNull(model.get("actors"));
		assertNull(model.get("genres"));
		assertNull(model.get("formErrors"));
		assertNull(model.get("newMovie"));
		assertTrue("The view should be a redirect view upon success", modelAndView.getView() instanceof RedirectView);
	}
}
