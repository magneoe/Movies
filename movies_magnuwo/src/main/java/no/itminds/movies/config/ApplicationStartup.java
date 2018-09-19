package no.itminds.movies.config;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.repository.MovieRepository;

@Component
@Transactional
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private MovieRepository movieRepo;
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		// Initializing DB
		Genre action = new Genre("Action");
		Genre comedy = new Genre("Comedy");
		Genre horror = new Genre("Horror");

		Actor actor1 = new Actor("Samuel Jackson");
		Actor actor2 = new Actor("Eddie Muprpy");
		Actor actor3 = new Actor("Uma Thurman");
		
		if (movieRepo.findByTitle("Pulp fiction") == null) {
			MovieBuilder movieBuilder = new MovieBuilder();
			Movie pulpFiction = movieBuilder
					.actors(Arrays.asList(actor1)).averageRating(new Float(5.0))
					.title("Pulp fiction").releaseDate(Date.valueOf("1994-10-21"))
					.created(Date.valueOf(LocalDate.now())).duration("120").genres(Arrays.asList(action, horror))
					.ratings(Arrays.asList(5, 5, 5)).plot("Everyone kills")
					.posterUrl(
							"https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SY1000_CR0,0,686,1000_AL_.jpg")
					.build();

			movieRepo.saveAndFlush(pulpFiction);
		}
		
		if(movieRepo.findByTitle("Coming to America") == null) {
			MovieBuilder movieBuilder = new MovieBuilder();
			Movie comingToAmerica = movieBuilder
					.actors(Arrays.asList(actor2)).averageRating(new Float(5.0))
					.title("Coming to America").releaseDate(Date.valueOf("1988-10-21"))
					.created(Date.valueOf(LocalDate.now())).duration("90").genres(Arrays.asList(comedy))
					.ratings(Arrays.asList(5, 5, 5)).plot("Very funny")
					.posterUrl(
							"https://m.media-amazon.com/images/M/MV5BNGZlNjdlZmMtYTg0MC00MmZkLWIyNDktYmNlOWYzMTkzYWQ1XkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1_SY1000_CR0,0,655,1000_AL_.jpg")
					.build();
			movieRepo.saveAndFlush(comingToAmerica);
		}
		
		if(movieRepo.findByTitle("Kill Bill") == null) {
			action = entityManager.merge(action);
			action = entityManager.find(Genre.class, action.getId());
			//comedy = entityManager.merge(comedy);
			
			
			MovieBuilder movieBuilder = new MovieBuilder();
			Movie killBill = movieBuilder
					.actors(Arrays.asList(actor3)).averageRating(new Float(5.0))
					.title("Kill Bill").releaseDate(Date.valueOf("2003-10-21"))
					.created(Date.valueOf(LocalDate.now())).duration("180").genres(Arrays.asList(action))
					.ratings(Arrays.asList(5, 5, 5)).plot("Very funny")
					.posterUrl(
							"https://m.media-amazon.com/images/M/MV5BNzM3NDFhYTAtYmU5Mi00NGRmLTljYjgtMDkyODQ4MjNkMGY2XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg")
					.build();

			movieRepo.saveAndFlush(killBill);
		}
	}

}
