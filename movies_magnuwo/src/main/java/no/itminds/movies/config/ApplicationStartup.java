package no.itminds.movies.config;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Movie.MovieBuilder;
import no.itminds.movies.model.Rating;
import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.repository.UserRepository;

@Component
@Transactional
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private MovieRepository movieRepo;

	@Autowired
	private UserRepository userRepo;

	@Lazy
	@Autowired
	private BCryptPasswordEncoder encoder;

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

		User user1 = null;
		if ((user1 = userRepo.findByEmail("magneoe@gmail.com")) == null) {
			user1 = new User("magneoe@gmail.com", encoder.encode("password"), "Magnus", "Østeng");
			entityManager.persist(user1);
		}
		Rating rating1 = new Rating(5, user1);
		Rating rating2 = new Rating(5, user1);
		Rating rating3 = new Rating(6, user1);

		if (movieRepo.findByTitle("Pulp fiction") == null) {
			MovieBuilder movieBuilder = new MovieBuilder();
			Movie pulpFiction = movieBuilder.actors(Arrays.asList(actor1)).averageRating(new Float(5.0))
					.title("Pulp fiction").releaseDate(Date.valueOf("1994-10-21"))
					.created(Date.valueOf(LocalDate.now())).duration("120").genres(Arrays.asList(action, horror))
					.ratings(Arrays.asList(rating1)).plot("Everyone kills")
					.posterUrl(
							"https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SY1000_CR0,0,686,1000_AL_.jpg")
					.build();

			movieRepo.save(pulpFiction);
		}

		if (movieRepo.findByTitle("Coming to America") == null) {
			MovieBuilder movieBuilder = new MovieBuilder();
			Movie comingToAmerica = movieBuilder.actors(Arrays.asList(actor2)).averageRating(new Float(5.0))
					.title("Coming to America").releaseDate(Date.valueOf("1988-10-21"))
					.created(Date.valueOf(LocalDate.now())).duration("90").genres(Arrays.asList(comedy))
					.ratings(Arrays.asList(rating2)).plot("Very funny")
					.posterUrl(
							"https://m.media-amazon.com/images/M/MV5BNGZlNjdlZmMtYTg0MC00MmZkLWIyNDktYmNlOWYzMTkzYWQ1XkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1_SY1000_CR0,0,655,1000_AL_.jpg")
					.build();
			movieRepo.save(comingToAmerica);
		}

		if (movieRepo.findByTitle("Kill Bill") == null) {
			MovieBuilder movieBuilder = new MovieBuilder();
			Movie killBill = movieBuilder.actors(Arrays.asList(actor3)).averageRating(new Float(5.0)).title("Kill Bill")
					.releaseDate(Date.valueOf("2003-10-21")).created(Date.valueOf(LocalDate.now())).duration("180")
					.genres(Arrays.asList(action, comedy)).ratings(Arrays.asList(rating3)).plot("Very funny")
					.posterUrl(
							"https://m.media-amazon.com/images/M/MV5BNzM3NDFhYTAtYmU5Mi00NGRmLTljYjgtMDkyODQ4MjNkMGY2XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg")
					.build();

			movieRepo.save(killBill);
		}
	}

}
