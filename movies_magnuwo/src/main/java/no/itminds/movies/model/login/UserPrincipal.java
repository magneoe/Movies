package no.itminds.movies.model.login;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

	private static Logger logger = LoggerFactory.getLogger(UserPrincipal.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -1952225599199212525L;
	private Collection<SimpleGrantedAuthority> authorities;
    private String email;
    private String password;
    private boolean active;
    private int failedLoginAttempts;
	
	public UserPrincipal(User user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.active = user.getActive();
		this.failedLoginAttempts = user.getFailedLoginAttempts();
		
		authorities = new ArrayList<>();
        user.getRoles().stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return (failedLoginAttempts < 3) ? true : false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return active;
	}
}
