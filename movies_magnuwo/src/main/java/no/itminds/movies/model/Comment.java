package no.itminds.movies.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import no.itminds.movies.model.login.User;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String title;
	
	private String comment;
	
	private Timestamp created;
	
	@NotNull
	@ManyToOne(fetch=FetchType.EAGER)
	private User author;

	public Comment() {}
	
	public Comment(String title, String comment, User author) {
		this.title = title;
		this.comment = comment;
		this.author = author;
		this.created = new Timestamp(System.currentTimeMillis());
	}
	public Comment(String title, String comment, Long id) {
		this.title = title;
		this.comment = comment;
		this.created = new Timestamp(System.currentTimeMillis());
		this.id = id;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}
}
