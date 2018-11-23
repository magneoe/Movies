package no.itminds.movies.model.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Movie;
import no.itminds.movies.model.login.User;

public class CommentDTO {
	@NotEmpty(message = "Title cannot be empty")
	private String title;
	@NotEmpty(message = "Comment cannot be empty")
	private String comment;

	private User author;

	// Formats output date when this DTO is passed through JSON
	@JsonFormat(pattern = Movie.DATE_TIME_PATTERN)
	// Allows format to be passed into GET request in JSON
	@DateTimeFormat(pattern = Movie.DATE_TIME_PATTERN)
	private LocalDateTime created;

	@NotNull
	private Long movieId;

	public CommentDTO() {
	}

	public CommentDTO(Comment comment, Long movieId) {
		super();
		if (comment != null) {
			this.title = comment.getTitle();
			this.comment = comment.getComment();
			this.movieId = movieId;
			this.author = comment.getAuthor();
			this.created = comment.getCreated();
		}
	}

	public CommentDTO(String title, String comment, Long movieId, LocalDateTime created, User author) {
		super();
		this.title = title;
		this.comment = comment;
		this.movieId = movieId;
		this.author = author;
		this.created = created;
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

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentDTO other = (CommentDTO) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (movieId == null) {
			if (other.movieId != null)
				return false;
		} else if (!movieId.equals(other.movieId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
