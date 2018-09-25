package no.itminds.movies.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.service.impl.DefaultMovieService;

public class MovieServiceTest {

	@Mock
	private MovieRepository movieRepo;

	@InjectMocks
	private DefaultMovieService movieService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAll() {
		// Arrange
		MovieBuilder builder = new MovieBuilder();
		builder.title("Title").year("1999").plot("plot");
		List<Movie> mockMovies = Arrays.asList(builder.build(), builder.build(), builder.build());

		// When
		Mockito.when(movieRepo.findAll()).thenReturn(mockMovies);

		// Then
		List<Movie> actualMovies = movieService.getAll();
		Mockito.verify(movieRepo).findAll();

		assertNotNull(actualMovies);
		assertNotNull(mockMovies);
		assertThat(actualMovies.size(), Is.is(mockMovies.size()));
	}

	@Test
	public void testGetDetails() {
		// Arrange
		final Long ID = new Long(5);

		Movie mockMovie = new Movie(ID);
		mockMovie.setPlot("Plot");
		mockMovie.setYear("1999");
		mockMovie.setTitle("Test title");

		// When
		Mockito.when(movieRepo.getOne(ID)).thenReturn(mockMovie);
		// Then
		Movie actualMovie = movieService.getDetails(ID);
		Mockito.verify(movieRepo).getOne(ID);

		assertNotNull(actualMovie);
		assertThat(actualMovie, IsEqual.equalTo(mockMovie));
		assertThat(actualMovie.getTitle(), IsEqual.equalTo(mockMovie.getTitle()));

	}

	@Test
	public void testPostComment() {
		// Arrange
		final Long MOVIE_ID = new Long(1);
		User newUser = new User("magneoe@gmail.com", "secret", "Magnus", "Østeng");
		
		// Expect exception
		expectedException.expect(PersistenceException.class);
		movieService.postComment(null, "Test", "Test", MOVIE_ID);

		// Expect exception
		expectedException.expect(PersistenceException.class);
		movieService.postComment(newUser, null, "Test", MOVIE_ID);

		// Expect exception
		expectedException.expect(PersistenceException.class);
		movieService.postComment(newUser, "Test", null, MOVIE_ID);

		Mockito.when(movieRepo.findById(null)).thenThrow(IllegalArgumentException.class);
		// Expect exception
		expectedException.expect(IllegalArgumentException.class);
		movieService.postComment(newUser, "Test", "Test", null);
	}

	@Test
	public void testVote() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCurrentRating() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

}
