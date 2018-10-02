package no.itminds.movies.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentDTO {
	@NotEmpty(message="Title cannot be empty")
	private String title;
	@NotEmpty(message="Comment cannot be empty")
	private String comment;

	@NotNull
	private Long movieId;

	public CommentDTO() {
	}

	public CommentDTO(String title, String comment, Long movieId) {
		super();
		this.title = title;
		this.comment = comment;
		this.movieId = movieId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
}
