package no.itminds.movies.model.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.Rating;

@JsonInclude(Include.NON_NULL)
public class MovieDTO {

	private Long id;

	@NotEmpty(message = "Title cannot be empty")
	private String title;
	@NotEmpty(message = "Year cannot be empty")
	private String year;
	@NotEmpty(message = "Plot cannot be empty")
	private String plot;

	private String contentRating;
	private String duration;
	private String imdbRating;
	private String posterUrl;

	// Formats output date when this DTO is passed through JSON
	@JsonFormat(pattern = Movie.DATE_PATTERN)
	// Allows dd/MM/yyyy date to be passed into GET request in JSON
	@DateTimeFormat(pattern = Movie.DATE_PATTERN)
	private LocalDate createdDate;

	// Formats output date when this DTO is passed through JSON
	@JsonFormat(pattern = Movie.DATE_PATTERN)
	// Allows dd/MM/yyyy date to be passed into GET request in JSON
	@DateTimeFormat(pattern = Movie.DATE_PATTERN)
	private LocalDate releaseDate;

	private double averageRating;

	private List<Genre> genres;
	private List<Actor> actors;

	@JsonIgnore
	private List<Rating> ratings;

	private static Logger LOGGER = LoggerFactory.getLogger(MovieDTO.class);

	public MovieDTO() {
		genres = new ArrayList<>();
		actors = new ArrayList<>();
		ratings = new ArrayList<>();
	}

	public MovieDTO(@NotEmpty(message = "Title cannot be empty") String title,
			@NotEmpty(message = "Year cannot be empty") String year,
			@NotEmpty(message = "Plot cannot be empty") String plot, String contentRating, String duration,
			String imdbRating, String posterUrl, LocalDate createdDate, LocalDate releaseDate, double averageRating,
			List<Genre> genres, List<Actor> actors, List<Rating> ratings) {
		this.title = title;
		this.year = year;
		this.plot = plot;
		this.contentRating = contentRating;
		this.duration = duration;
		this.imdbRating = imdbRating;
		this.posterUrl = posterUrl;
		this.createdDate = createdDate;
		this.releaseDate = releaseDate;
		this.genres = genres;
		this.actors = actors;
		this.ratings = ratings;
		this.averageRating = averageRating;
	}

	public MovieDTO(Movie fromMovie) {
		if (fromMovie != null) {
			this.id = fromMovie.getId();
			this.title = fromMovie.getTitle();
			this.year = fromMovie.getYear();
			this.plot = fromMovie.getPlot();
			this.contentRating = fromMovie.getContentRating();
			this.duration = fromMovie.getDuration();
			this.imdbRating = fromMovie.getImdbRating();
			this.posterUrl = fromMovie.getPosterUrl();
			this.createdDate = fromMovie.getCreated();
			this.releaseDate = fromMovie.getReleaseDate();
			this.genres = fromMovie.getGenres();
			this.actors = fromMovie.getActors();
			this.ratings = fromMovie.getRatings();
			this.averageRating = fromMovie.getAverageRating();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public String getContentRating() {
		return contentRating;
	}

	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Actor> getActors() {
		return actors;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieDTO other = (MovieDTO) obj;
		if (actors == null) {
			if (other.actors != null)
				return false;
		} else if (!actors.equals(other.actors))
			return false;
		if (Double.doubleToLongBits(averageRating) != Double.doubleToLongBits(other.averageRating))
			return false;
		if (contentRating == null) {
			if (other.contentRating != null)
				return false;
		} else if (!contentRating.equals(other.contentRating))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (genres == null) {
			if (other.genres != null)
				return false;
		} else if (!genres.equals(other.genres))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imdbRating == null) {
			if (other.imdbRating != null)
				return false;
		} else if (!imdbRating.equals(other.imdbRating))
			return false;
		if (plot == null) {
			if (other.plot != null)
				return false;
		} else if (!plot.equals(other.plot))
			return false;
		if (posterUrl == null) {
			if (other.posterUrl != null)
				return false;
		} else if (!posterUrl.equals(other.posterUrl))
			return false;
		if (ratings == null) {
			if (other.ratings != null)
				return false;
		} else if (!ratings.equals(other.ratings))
			return false;
		if (releaseDate == null) {
			if (other.releaseDate != null)
				return false;
		} else if (!releaseDate.equals(other.releaseDate))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MovieDTO [id=" + id + ", title=" + title + ", year=" + year + ", plot=" + plot + ", contentRating="
				+ contentRating + ", duration=" + duration + ", imdbRating=" + imdbRating + ", posterUrl=" + posterUrl
				+ ", createdDate=" + createdDate + ", releaseDate=" + releaseDate + ", averageRating=" + averageRating
				+ ", genres=" + genres + ", actors=" + actors + ", ratings=" + ratings + "]";
	}
}
