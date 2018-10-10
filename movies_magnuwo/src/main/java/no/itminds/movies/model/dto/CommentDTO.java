package no.itminds.movies.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import no.itminds.movies.model.login.User;

public class CommentDTO {
	@NotEmpty(message="Title cannot be empty")
	private String title;
	@NotEmpty(message="Comment cannot be empty")
	private String comment;
	
	private User author;

	@NotNull
	private Long movieId;

	public CommentDTO() {
	}

	public CommentDTO(String title, String comment, Long movieId, User author) {
		super();
		this.title = title;
		this.comment = comment;
		this.movieId = movieId;
		this.author = author;
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

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
}
