package no.itminds.movies.model.login;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class User {

	@JsonProperty(access=Access.WRITE_ONLY)
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@JsonProperty(access=Access.WRITE_ONLY)
	@Email(message="Please provide a valid email")
	@NotEmpty(message="Please provide a email")
	@Column(unique=true)
	private String email;
	
	@JsonProperty(access=Access.WRITE_ONLY)
	@NotEmpty(message="Password can not be empty")
	@Length(min=6 , message="The password must be minimum 6 characters long")
	private String password;
	
	@JsonProperty(access=Access.WRITE_ONLY)
	private Integer failedLoginAttempts = 0;

	
	@NotEmpty(message="Name cannot be empty")
	private String name;
	
	@NotEmpty(message="Lastname cannot be empty")
	private String lastname;
	
	@JsonProperty(access=Access.WRITE_ONLY)
	private Boolean active;
	
	@JsonProperty(access=Access.WRITE_ONLY)
	@ManyToMany(fetch=FetchType.EAGER, targetEntity=Role.class, cascade=CascadeType.ALL)
	private Set<Role> roles = new HashSet<>();
	
	@JsonProperty(access=Access.WRITE_ONLY)
	private LocalDateTime created;

	public User() {}
	
	public User(
			@Email(message = "Please provide a valid email") @NotEmpty(message = "Please provide a email") String email,
			@NotEmpty(message = "Password can not be empty") @Length(min = 6, message = "The password must be minimum 6 characters long") String password,
			@NotEmpty(message = "Name cannot be empty") String name,
			@NotEmpty(message = "Lastname cannot be empty") String lastname) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.created = LocalDateTime.now();
		this.active = true;
		this.failedLoginAttempts = 0;
	}



	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(Integer failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public void addRole(Role role) {
		roles.add(role);
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	
	/*
	 * User methods - non get/setters
	 */
	public boolean hasAdminRights() {
		IntPredicate predicateHasAdminAccess = (accessLevel) -> { 
			if(accessLevel == Role.ACCESS_LEVEL_ADMIN) 
				return true; 
			else 
				return false;};
		return roles.stream().
			mapToInt(Role::getAccessLevel).
			anyMatch(predicateHasAdminAccess);
	}
	
	
	public boolean equals(Object obj) {
		if(!(obj instanceof User))
			return false;
		
		User user = (User) obj;
		if(user.getId() != getId())
			return false;
		if(user.getEmail() != getEmail())
			return false;
		return true;
	}
}
