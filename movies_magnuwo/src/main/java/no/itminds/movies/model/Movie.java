package no.itminds.movies.model;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private String year;
	@OneToMany
	private List<Genre> genres;
	@ElementCollection(fetch=FetchType.EAGER)
	 @CollectionTable(
	       name="RATINGS",
	        joinColumns=@JoinColumn(name="MOVIE_ID")
	  )
	private List<Integer> ratings;
	private String contentRating;
	private String duration;
	private Date releaseDate;
	private double averageRating;
	private String orginalTitle;
	private String storyLine;
	@OneToMany
	private List<Actor> actors;
	private String imdbRating;
	private String posterUrl;
	private String plot;
	
	public Movie() {}
	
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

	public List<Integer> getRatings() {
		return ratings;
	}

	public void setRatings(List<Integer> ratings) {
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
	
	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", year=" + year + ", genres=" + genres + ", ratings=" + ratings
				+ ", contentRating=" + contentRating + ", duration=" + duration + ", releaseDate=" + releaseDate
				+ ", averageRating=" + averageRating + ", orginalTitle=" + orginalTitle + ", storyLine=" + storyLine
				+ ", actors=" + actors + ", imdbRating=" + imdbRating + ", posterUrl=" + posterUrl + ", plot=" + plot
				+ "]";
	}

	public static class MovieBuilder {
		private String title;
		private String year;
		private List<Genre> genres;
		private List<Integer> ratings;
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
		
		public MovieBuilder title(String title) {
			this.title = title;
			return this;
		}
		public MovieBuilder year(String year) {
			this.year = year;
			return this;
		}
		public MovieBuilder ratings(List<Integer> ratings) {
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
		/*public MovieBuilder averageRating(float averageRating) {
			this.averageRating = averageRating;
			return this;
		}*/
		public Movie build() {
			return new Movie(this);
		}
	}
	
}
