package no.itminds.movies.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import no.itminds.movies.model.login.User;

@Entity
public class Rating {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@NotNull
	private Integer rating;
	
	@JsonIgnore
	@NotNull
	@ManyToOne
	private User author;

	public Rating() {}
	
	public Rating(int rating, User author) {
		this.rating = rating;
		this.author = author;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
	
}
