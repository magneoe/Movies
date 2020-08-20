package no.itminds.movies.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
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

import no.itminds.movies.exceptions.NotFoundException;
import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.service.impl.DefaultMovieService;

public class MovieServiceTest {

	@Mock
	private MovieRepository movieRepo;
	
	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private DefaultMovieService movieService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private User user, user2;
	
	private List<Movie> testMovies;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		
		user = new User("magne@gmail.com", "secret", "Bob", "Dyland");
		user2 = new User("magne@testmail.com", "password", "Harry", "Hole");
		
		//Build three movies
		MovieBuilder builder = new MovieBuilder();
		
		Movie movie1 = builder.title("Pulp fiction").year("1999").plot("plot").build();
		
		Movie movie2 = builder.title("Comming to America").year("1988").plot("plot2").build();
		
		Movie movie3 = builder.title("Kill Bill").year("1960").plot("plot3").build();
		
		testMovies = Arrays.asList(movie1, movie2, movie3);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAll() {
		// Arrange
		final int TEST_MOVIE_LIST_SIZE = testMovies.size();
		// When
		Mockito.when(movieRepo.findAll()).thenReturn(testMovies);

		// Then
		List<Movie> actualMovies = movieService.getAll();
		Mockito.verify(movieRepo).findAll();

		assertNotNull(actualMovies);
		assertNotNull(testMovies);
		assertThat(actualMovies.size(), Is.is(TEST_MOVIE_LIST_SIZE));
	}

	@Test
	public void testGetDetails() {
		// Arrange
		final Long ID = new Long(5);

		Movie mockMovie = new Movie(ID);
		mockMovie.setPlot("Plot");
		mockMovie.setYear("1999");
		mockMovie.setTitle("Test title");
		
		Optional<Movie> mockMovieOpt = Optional.of(mockMovie);

		// When
		Mockito.when(movieRepo.findById(ID)).thenReturn(mockMovieOpt);
		// Then
		Movie actualMovie = movieService.getDetails(ID);
		Mockito.verify(movieRepo).findById(ID);

		assertNotNull(actualMovie);
		assertThat(actualMovie, IsEqual.equalTo(mockMovie));
		assertThat(actualMovie.getTitle(), IsEqual.equalTo(mockMovie.getTitle()));

	}

	@Test
	public void testPostComment() {
		// Arrange
		final Long MOVIE_ID = new Long(1);
		Movie mockMovie = new Movie(MOVIE_ID);
		Optional<Movie> mockMovieOpt = Optional.of(mockMovie);
		
		/*
		 * Test 1 - happy day scenario
		 */
		//When 
		Mockito.when(movieRepo.findById(MOVIE_ID)).thenReturn(mockMovieOpt);
		Mockito.when(movieRepo.findById(null)).thenThrow(NotFoundException.class);
		
		movieService.postComment(user, new Comment("Test", "Test2", MOVIE_ID));
		
		Comment comment = mockMovie.getComments().get(0);
		//Then
		assertNotNull(comment);
		assertThat(comment.getTitle(), Is.is("Test"));
		assertThat(comment.getComment(), Is.is("Test2"));
		assertTrue(LocalDateTime.now().minusMinutes(2).isBefore(comment.getCreated().toLocalDateTime()));
		
		/*
		 * Test 2 - 
		 */
		expectedException.expect(PersistenceException.class);
		movieService.postComment(null, new Comment("Test", "Test", MOVIE_ID));
	
		/*
		 * Test 3 - invalid input
		 */
		expectedException.expect(NotFoundException.class);
		movieService.postComment(user, new Comment("Test", "Test", new Long(-1)));
	}

	@Test
	public void testVote() {
		//Arrange
		final Long MOCK_MOVIE_ID = new Long(10);
		Movie mockMovie = new Movie(MOCK_MOVIE_ID);
		mockMovie.setTitle("Pulp fiction");
		mockMovie.setPlot("Plot");
		mockMovie.setYear("1955");
		mockMovie.addRating(new Rating(7, user2));
		
		Optional<Movie> movieOpt = Optional.of(mockMovie);
		
		/*
		 * Test 1 - valid input. New user rates a movie
		 */
		//When
		Mockito.when(movieRepo.findById(MOCK_MOVIE_ID)).thenReturn(movieOpt);
		
		movieService.vote(user, MOCK_MOVIE_ID, new Integer(4));
		
		//Then
		Mockito.verify(movieRepo).findById(MOCK_MOVIE_ID);
		Mockito.verify(movieRepo).saveAndFlush(mockMovie);
		
		//Check calculated average rating
		assertEquals(((double)4+7)/2, mockMovie.getAverageRating(), 0);
		
		/*
		 * Test 2 - valid input. Same user rates the movie again with new value
		 */
		movieService.vote(user, MOCK_MOVIE_ID, new Integer(9));
		
		assertEquals(((double)9+7)/2, mockMovie.getAverageRating(), 0);
		
		/*
		 * Test 3 - invalid input
		 */
		expectedException.expect(IllegalArgumentException.class);
		movieService.vote(null, MOCK_MOVIE_ID, new Integer(9));
		
		expectedException.expect(IllegalArgumentException.class);
		movieService.vote(user, null, new Integer(9));
		
		expectedException.expect(IllegalArgumentException.class);
		movieService.vote(user, MOCK_MOVIE_ID, null);

	}

	@Test
	public void testGetCurrentRating() {
		//Arrange
		final Long MOCK_MOVIE_ID = new Long(10);
		Movie mockMovie = new Movie(MOCK_MOVIE_ID);
		mockMovie.setTitle("Pulp fiction");
		mockMovie.setPlot("Plot");
		mockMovie.setYear("1955");
		mockMovie.addRating(new Rating(6, user));
		
		/*
		 * Test 1 -valid input
		 */
		//When
		Rating currentRating = movieService.getCurrentRating(user, mockMovie);
		
		//Then
		assertThat(currentRating.getAuthor().getEmail(), Is.is("magne@gmail.com"));
		assertThat(currentRating.getRating(), Is.is(6));
		assertNotNull(mockMovie);
		
		/*
		 * Test 2 - invalid input
		 */
		currentRating = movieService.getCurrentRating(user, null);
		
		assertNull(currentRating);
		
		currentRating = movieService.getCurrentRating(null, mockMovie);
		
		assertNull(currentRating);
	}

	@Test
	public void testSave() throws Exception{
		
		//Arrange
		Movie newMovie = testMovies.get(0);
		
		Movie mockMovieOnSaveAndFlush = new Movie(new Long(5));
		
		//When
		Mockito.when(entityManager.merge(newMovie)).thenReturn(mockMovieOnSaveAndFlush);
		
		Long id = movieService.save(newMovie);
		
		/*
		 * Test 1 - happy day scenario
		 */
		//Then
		Mockito.verify(entityManager).merge(newMovie);
		
		assertThat(id, Is.is(new Long(5)));
		
		/*
		 * Test 2 - invalid input
		 */
		
		expectedException.expect(PersistenceException.class);
		id = movieService.save(null);
		assertNull(id);
		
	}

}
