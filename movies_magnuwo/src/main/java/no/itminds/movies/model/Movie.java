package no.itminds.movies.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotEmpty(message="Title cannot be empty")
	private String title;
	@NotEmpty(message="Year cannot be empty")
	private String year;
	@ManyToMany(cascade=CascadeType.PERSIST)
	private List<Genre> genres;

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="movie_id")
	private List<Rating> ratings = new ArrayList<>();
	private String contentRating;
	private String duration;
	
	private Date releaseDate;
	@Transient
	private String releaseDateDTO;
	
	private double averageRating;
	private String orginalTitle;
	private String storyLine;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	private List<Actor> actors;
	private String imdbRating;
	private String posterUrl;
	
	@NotEmpty(message="Plot cannot be empty")
	private String plot;
	
	private Date created;
	@Transient
	private String createdDTOString;
	
	@OneToMany(fetch=FetchType.LAZY, cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Comment> comments = new ArrayList<>();
	
	public Movie() {
	}
	
	public Movie(Long id) {
		this.id = id;
	}
	
	public Movie(MovieBuilder builder) {
		this.title = builder.title;
		this.year = builder.year;
		this.genres = builder.genres;
		this.ratings = builder.ratings;
		this.contentRating = builder.contentRating;
		this.duration = builder.duration;
		this.releaseDate = builder.releaseDate;
		this.averageRating = builder.averageRating;
		this.orginalTitle = builder.orginalTitle;
		this.storyLine = builder.storyLine;
		this.actors = builder.actors;
		this.imdbRating = builder.imdbRating;
		this.posterUrl = builder.posterUrl;
		this.plot = builder.plot;
		this.created = builder.createdDate;
		this.comments = builder.comments;
	}
	
	public Long getId() {
		return this.id;
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

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
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

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public String getOrginalTitle() {
		return orginalTitle;
	}

	public void setOrginalTitle(String orginalTitle) {
		this.orginalTitle = orginalTitle;
	}

	public String getStoryLine() {
		return storyLine;
	}

	public void setStoryLine(String storyLine) {
		this.storyLine = storyLine;
	}

	public List<Actor> getActors() {
		return actors;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
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

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	public void setCreated(String created) throws IllegalArgumentException, DateTimeParseException{
		this.created = Date.valueOf(LocalDate.parse(created, DateTimeFormatter.ofPattern("dd-mm-yyyy")));
	}
	public void setReleaseDate(String releaseDate) throws IllegalArgumentException, DateTimeParseException{
		this.releaseDate = Date.valueOf(LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("dd-mm-yyyy")));
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public void addComment(Comment comment) {
		comments.add(comment);
	}
	
	public void addRating(Rating newRating) {
		ratings.add(newRating);
	}
	
	public String getReleaseDateDTO() {
		return releaseDateDTO;
	}

	public void setReleaseDateDTO(String releaseDateDTO) {
		this.releaseDateDTO = releaseDateDTO;
	}

	public String getCreatedDTOString() {
		return createdDTOString;
	}

	public void setCreatedDTOString(String createdDTOString) {
		this.createdDTOString = createdDTOString;
	}

	public void calculateNewAverage() {
		OptionalDouble avgOpt = ratings.stream().
				mapToInt(rating -> rating.getRating()).
				average();
			if(avgOpt.isPresent()) {
				averageRating = avgOpt.getAsDouble();
			}
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Movie))
			return false;
		Movie m = (Movie)obj;
		
		if(!m.getTitle().equals(title))
			return false;
		if(!m.getId().equals(id))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", year=" + year + ", genres=" + genres + ", ratings=" + ratings
				+ ", contentRating=" + contentRating + ", duration=" + duration + ", releaseDate=" + releaseDate
				+ ", averageRating=" + averageRating + ", orginalTitle=" + orginalTitle + ", storyLine=" + storyLine
				+ ", actors=" + actors + ", imdbRating=" + imdbRating + ", posterUrl=" + posterUrl + ", plot=" + plot
				+ ", created=" + created + ", comments=" + comments + "]";
	}






	public static class MovieBuilder {
		private String title;
		private String year;
		private List<Genre> genres;
		private List<Rating> ratings;
		private String contentRating;
		private String duration;
		private Date releaseDate;
		private double averageRating;
		private String orginalTitle;
		private String storyLine;
		private List<Actor> actors;
		private String imdbRating;
		private String posterUrl;
		private String plot;
		private Date createdDate;
		private List<Comment> comments;
		
		public MovieBuilder title(String title) {
			this.title = title;
			return this;
		}
		public MovieBuilder year(String year) {
			this.year = year;
			return this;
		}
		public MovieBuilder genres(List<Genre> genres) {
			this.genres = genres;
			return this;
		}
		public MovieBuilder ratings(List<Rating> ratings) {
			this.ratings = ratings;
			return this;
		}
		
		public MovieBuilder contentRating(String contentRating) {
			this.contentRating = contentRating;
			return this;
		}
		
		public MovieBuilder duration(String duration) {
			this.duration = duration;
			return this;
		}
		public MovieBuilder releaseDate(Date releaseDate) {
			this.releaseDate = releaseDate;
			return this;
		}
		public MovieBuilder averageRating(float averageRating) {
			this.averageRating = averageRating;
			return this;
		}
		public MovieBuilder orginalTitle(String orginalTitle) {
			this.orginalTitle = orginalTitle;
			return this;
		}
		public MovieBuilder storyLine(String storyLine) {
			this.storyLine = storyLine;
			return this;
		}
		public MovieBuilder actors(List<Actor> actors) {
			this.actors = actors;
			return this;
		}
		public MovieBuilder imdbRating(String imdbRating) {
			this.imdbRating = imdbRating;
			return this;
		}
		public MovieBuilder posterUrl(String posterUrl) {
			this.posterUrl = posterUrl;
			return this;
		}
		public MovieBuilder plot(String plot) {
			this.plot = plot;
			return this;
		}
		public MovieBuilder created(Date created) {
			this.createdDate = created;
			return this;
		}
		public MovieBuilder comments(List<Comment> comments) {
			this.comments = comments;
			return this;
		}
		public Movie build() {
			return new Movie(this);
		}
	}
	
}
