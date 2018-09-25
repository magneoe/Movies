package no.itminds.movies.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Actor implements Comparable<Actor>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	
	public Actor() {}
	public Actor(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return name;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public int compareTo(Actor o) {
		if(id == o.getId())
			return 0;
		return name.compareTo(o.getName());
	}
}
