package no.itminds.movies.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import no.itminds.movies.model.login.User;

public interface UserService extends UserDetailsService {
	public UserDetails loadUserByUsername(String email);
	public UserDetails loadUserById(Long id);
	public User loadUserByEmail(String email);
}
