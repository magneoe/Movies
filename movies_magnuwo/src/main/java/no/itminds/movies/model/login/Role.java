package no.itminds.movies.model.login;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

	protected final static int ACCESS_LEVEL_USER = 1;
	protected final static int ACCESS_LEVEL_ADMIN = 2;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(unique=true)
	private String role;
	@Column(unique=true)
	private int accessLevel;
	 
	public Role() {
		accessLevel = ACCESS_LEVEL_USER;
	}
	public Role(String role) {
		this.role = role;
		accessLevel = ACCESS_LEVEL_USER;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	protected int getAccessLevel() {
		return accessLevel;
	}
	protected void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	
}

