package no.itminds.movies.test.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import no.itminds.movies.model.Movie;
import no.itminds.movies.repository.MovieRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(
		  locations = "classpath:test-integration.properties")
public class MovieRepositoryTest {
	
	@Autowired
	private MovieRepository movieRepository;
	
	final String MOVIE_TITLE = "Pulp fiction";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Transactional
	@Rollback(value=true)
	@Test
	public void testGetAllMovies() {
		//When
		List<Movie> actualMovies = movieRepository.findAll();
		//Then
		assertThat("The number of movies should be 3", actualMovies.size(), is(3));
		assertThat("There should be one movie with title Pulp Fiction", 
				actualMovies.stream().anyMatch(movie -> movie.getTitle().equals(MOVIE_TITLE)),
				is(true));
	}
	
	@Transactional
	@Rollback(value=true)
	@Test
	public void testFindByTitle()
	{
		//When
		Movie actualMovie = movieRepository.findByTitle(MOVIE_TITLE);
		
		//Then
		assertNotNull(actualMovie);
		assertThat(actualMovie.getTitle(), IsEqual.equalTo(MOVIE_TITLE));
		assertThat(Date.valueOf(actualMovie.getReleaseDate()).getTime(), IsEqual.equalTo(Date.valueOf("1994-10-21").getTime()));
		
	}

}
