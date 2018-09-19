package no.itminds.movies.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import no.itminds.movies.controller.MovieController;
import no.itminds.movies.model.Movie;
import no.itminds.movies.service.MovieService;

public class MovieControllerTest {

	@InjectMocks
	private MovieController movieController;
	@Mock
	private MovieService movieService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	@Test
    public void testGetAll()
    {
		//Arrange
		List<Movie> moviesMocks = Arrays.asList(new Movie(), new Movie(), new Movie());
		when(movieService.getAll()).thenReturn(moviesMocks);
        
        //Test
		ModelAndView result = movieController.index();
        Map<String, Object> model = result.getModel();
        verify(movieService).getAll();
        
        
        assertTrue(model.containsKey("movies"));
        
        Object moviesObj = model.get("movies");
        if(!(moviesObj instanceof List)) {
        	fail("The entry movies in the model should be a list");
        }
        List<Movie> moviesActual = (List) moviesObj;
        assertEquals(moviesMocks.size(), moviesActual.size());
        
    }
	
	@Test
	public void testGetDetails() {
		Long movieId = new Long(1);
		//Arrange
		Movie movieMock = new Movie(movieId);
		when(movieService.getDetails(movieId)).thenReturn(movieMock);
		
		//Test
		ModelAndView modelAndView = movieController.getDetails(movieId);
		Map<String, Object> model = modelAndView.getModel();
		
		verify(movieService).getDetails(movieId);
		
		assertTrue(model.containsKey("movie"));
		
		Object movieObj = model.get("movie");
        if(!(movieObj instanceof Movie)) {
        	fail("The entry movie in the model should be a Movie object");
        }
        Movie actualMovie = (Movie) movieObj;
        assertThat(movieId, is(actualMovie.getId()));
	}
}
