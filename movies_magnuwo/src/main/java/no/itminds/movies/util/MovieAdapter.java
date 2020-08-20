package no.itminds.movies.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.dto.MovieDTO;

public class MovieAdapter {
	
	public final static String DATE_PATTERN = "dd-MM-yyyy";
	private final static Logger logger = Logger.getLogger(MovieAdapter.class);

	
	public static Movie fromMovieDTO(MovieDTO movieDTO, List<Actor> actorReferenceList) {
		Movie movie = new Movie();
		
		movie.setTitle(movieDTO.getTitle());
		movie.setYear(movieDTO.getYear());
		movie.setGenres(movieDTO.getGenres());
		movie.setContentRating(movieDTO.getContentRating());
		movie.setDuration(movieDTO.getDuration());

		movie.setOrginalTitle(movieDTO.getOrginalTitle());
		movie.setStoryLine(movieDTO.getStoryLine());

		String[] actorsAsString = movieDTO.getActors();
		Arrays.stream(actorsAsString).forEach(actorName -> {
			Optional<Actor> actorOpt = actorReferenceList.stream().
					filter(actor -> actor.getName().equals(actorName)).findFirst();
			if(actorOpt.isPresent()) {
				movie.getActors().add(actorOpt.get());
			}
		});
				
		movie.setImdbRating(movieDTO.getImdbRating());
		movie.setPosterUrl(movieDTO.getPosterUrl());
		movie.setPlot(movieDTO.getPlot());
		try {
			movie.setReleaseDate(Date.valueOf(
					LocalDate.parse(movieDTO.getReleaseDate(), DateTimeFormatter.ofPattern(DATE_PATTERN))));
			movie.setCreated(Date.valueOf(
					LocalDate.parse(movieDTO.getCreatedDate(), DateTimeFormatter.ofPattern(DATE_PATTERN))));
		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}
		return movie;
	}
}
