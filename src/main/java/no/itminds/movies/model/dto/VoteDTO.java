package no.itminds.movies.model.dto;

public class VoteDTO {
	private Integer rating;
	private Long movieId;
	
	public VoteDTO() {}
	
	public VoteDTO(Integer rating, Long movieId) {
		super();
		this.rating = rating;
		this.movieId = movieId;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Long getMovieId() {
		return movieId;
	}
	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
	
}
