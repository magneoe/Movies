package no.itminds.movies.model.login;

import java.sql.Timestamp;
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

import no.itminds.movies.model.AuthProvider;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Email(message="Please provide a valid email")
	@NotEmpty(message="Please provide a email")
	@Column(unique=true)
	private String email;
	
	@NotEmpty(message="Password can not be empty")
	@Length(min=6 , message="The password must be minimum 6 characters long")
	private String password;
	
	private Integer failedLoginAttempts = 0;

	@NotEmpty(message="Name cannot be empty")
	private String name;
	
	@NotEmpty(message="Lastname cannot be empty")
	private String lastname;
	
	private Boolean active;
	
	@ManyToMany(fetch=FetchType.EAGER, targetEntity=Role.class, cascade=CascadeType.ALL)
	private Set<Role> roles = new HashSet<>();
	
	private Timestamp created;
	
	private AuthProvider authProvider;

	private String providerId;

	private String imageUrl;

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
		this.created = Timestamp.valueOf(LocalDateTime.now());
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

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
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

	public AuthProvider getProvider() {
		return this.authProvider;
	}

	public void setProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
		
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
		
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		
	}
}
