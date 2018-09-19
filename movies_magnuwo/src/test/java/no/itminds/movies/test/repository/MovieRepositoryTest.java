package no.itminds.movies.test.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import no.itminds.movies.model.Movie;
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

	@Test
	public void testGetAllMovies() {
		//Given
		entityManager.merge(new Movie(new Long(1)));
		entityManager.merge(new Movie(new Long(2)));
		entityManager.merge(new Movie(new Long(3)));
		entityManager.flush();
		
		//When
		List<Movie> actualMovies = movieRepository.findAll();
		//Then
		assertThat("The number of movies should be 3", actualMovies.size(), is(3));
	}

}
