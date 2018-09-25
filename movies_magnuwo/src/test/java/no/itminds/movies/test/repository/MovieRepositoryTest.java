package no.itminds.movies.test.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.repository.MovieRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(
		  locations = "classpath:test-integration.properties")
public class MovieRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private MovieRepository movieRepository;
	
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
		MovieBuilder builder = new MovieBuilder();
		builder.plot("Test plot").year("1999").title("Test title");
		//Given
		entityManager.persist(builder.build());
		entityManager.persist(builder.build());
		entityManager.persist(builder.build());
		entityManager.flush();
		
		//When
		List<Movie> actualMovies = movieRepository.findAll();
		//Then
		assertThat("The number of movies should be 3", actualMovies.size(), is(3));
	}
	
	@Transactional
	@Rollback(value=true)
	@Test
	public void testFindByTitle()
	{
		final String title = "Pulp fiction";
		//Given
		MovieBuilder builder = new MovieBuilder();
		builder.title(title);
		builder.plot("Test plot");
		builder.year("1995");
	
		Movie persistedEntity = entityManager.persistAndFlush(builder.build());
		
		//When
		Movie actualMovie = movieRepository.findByTitle("Pulp fiction");
		
		//Then
		assertNotNull(actualMovie);
		assertNotNull(persistedEntity);
		assertThat(actualMovie.getTitle(), IsEqual.equalTo(title));
		assertThat(actualMovie, IsEqual.equalTo(persistedEntity));
		
	}

}
