package no.itminds.movies.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
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
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.CommentRepository;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.service.impl.DefaultMovieService;

public class MovieServiceTest {

	@Mock
	private MovieRepository movieRepo;
	
	@Mock
	private CommentRepository commentRepo;
	
	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private DefaultMovieService movieService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private User user, user2;
	
	private List<Movie> testMovies;
	private MovieDTO testDTOMovie;
	
	private final LocalDate dateNow = LocalDate.now();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		
		user = new User("test@gmail.com", "secret", "Bob", "Dyland");
		user2 = new User("test2@testmail.com", "password", "Harry", "Hole");
		
		//Build three movies
		MovieBuilder builder = new MovieBuilder();
		
		Movie movie1 = builder.title("Pulp fiction").year("1999").plot("plot").build();
		
		Movie movie2 = builder.title("Comming to America").year("1988").plot("plot2").build();
		
		Movie movie3 = builder.title("Kill Bill").year("1960").plot("plot3").build();
		
		testMovies = Arrays.asList(movie1, movie2, movie3);
		
		//Build one DTO movies
		testDTOMovie = new MovieDTO("Test", "1945", "Something happens", 
				"Good", "46", "3.6", "testUrl", dateNow, dateNow, 3.0, null, null, null);
		
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
		Movie mockMovie = Mockito.spy(new Movie(MOVIE_ID));
		Comment mockComment = new Comment("TestTitle", "TestComment", user);
		Optional<Movie> mockMovieOpt = Optional.of(mockMovie);
		
		/*
		 * Test 1 - happy day scenario
		 */
		//When 
		Mockito.when(movieRepo.findById(MOVIE_ID)).thenReturn(mockMovieOpt);
		Mockito.when(commentRepo.save(Mockito.any())).thenReturn(mockComment);
		Mockito.when(movieRepo.saveAndFlush(mockMovie)).thenReturn(mockMovie);
		Mockito.when(movieRepo.findById(null)).thenThrow(NotFoundException.class);
		
		Comment comment = movieService.postComment(user, mockComment.getTitle(), mockComment.getComment(), MOVIE_ID);
		
		//Then
		Mockito.verify(mockMovie).addComment(Mockito.any());
		assertNotNull("A valid comment should be returned", comment);
		assertThat(comment.getTitle(), Is.is("TestTitle"));
		assertThat(comment.getComment(), Is.is("TestComment"));
		assertTrue(LocalDateTime.now().minusMinutes(2).isBefore(comment.getCreated()));
		
		/*
		 * Test 2 - 
		 */
		expectedException.expect(IllegalArgumentException.class);
		movieService.postComment(null, "Test", "Test", MOVIE_ID);
	
		/*
		 * Test 3 - invalid input
		 */
		expectedException.expect(NotFoundException.class);
		movieService.postComment(user, "Test", "Test", null);
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
	public void testGetCurrentRating_validInput() {
		//Arrange
		final Long MOCK_MOVIE_ID = new Long(10);
		Movie mockMovie = new Movie(MOCK_MOVIE_ID);
		Optional<Movie> optionalOfMovie = Optional.of(mockMovie);
		
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
		assertThat(currentRating.getAuthor().getEmail(), Is.is("test@gmail.com"));
		assertThat(currentRating.getRating(), Is.is(6));
		assertNotNull(mockMovie);
		
		/*
		 * Overloaded method
		 * Parameters: User currentUser and Long movieId
		 */
		//When
		Mockito.when(movieRepo.findById(MOCK_MOVIE_ID)).thenReturn(optionalOfMovie);
		currentRating = movieService.getCurrentRating(user, MOCK_MOVIE_ID);
		
		//Then
		assertThat(currentRating.getAuthor().getEmail(), Is.is("test@gmail.com"));
		assertThat(currentRating.getRating(), Is.is(6));
		assertNotNull(mockMovie);
	}
	@Test
	public void testGetCurrentRating_invalidInput() {
		//Arrange
		final Long MOCK_MOVIE_ID = new Long(10);
		Movie mockMovie = new Movie(MOCK_MOVIE_ID);
		mockMovie.setTitle("Pulp fiction");
		mockMovie.setPlot("Plot");
		mockMovie.setYear("1955");
		
		Rating currentRating;
		/*
		 * Test 1 
		 */
		expectedException.expect(IllegalArgumentException.class);
		currentRating = movieService.getCurrentRating(user, new Movie());
		
		assertNotNull(currentRating);
		assertEquals(currentRating.getAuthor(), null);
		assertEquals(currentRating.getRating(), null);
		
		/*
		 * Test 2
		 */
		
		expectedException.expect(IllegalArgumentException.class);
		currentRating = movieService.getCurrentRating(null, mockMovie);
		
		assertNull(currentRating);
		
		/*
		 * Test 3
		 */
		
		currentRating = movieService.getCurrentRating(user, mockMovie);
		
		assertNull(currentRating);
		
		/*
		 * Test 4
		 */
		mockMovie.addRating(new Rating(6, user2));
		currentRating = movieService.getCurrentRating(user, mockMovie);
		
		assertNull(currentRating);
		
	}
	

	@Test
	public void testSave() {
		
		//Arrange
		MovieBuilder builder = new MovieBuilder();
		Movie newMovie = builder.fromMovieDTO(testDTOMovie).build();
		
		builder.id(new Long(5));
		Movie mockMovieOnSaveAndFlush = builder.build();
		
		//When
		Mockito.when(entityManager.merge(newMovie)).thenReturn(mockMovieOnSaveAndFlush);
		
		Movie persistedMovie = movieService.save(testDTOMovie);
		
		/*
		 * Test 1 - happy day scenario
		 */
		//Then
		Mockito.verify(entityManager, Mockito.times(1)).merge(newMovie);
		
		assertThat(persistedMovie.getId(), Is.is(new Long(5)));
		
		/*
		 * Test 2 - invalid input
		 */
		
		expectedException.expect(IllegalArgumentException.class);
		persistedMovie = movieService.save(null);
		assertNull(persistedMovie);
		
	}

}
