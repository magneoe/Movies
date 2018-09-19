package no.itminds.movies.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import no.itminds.movies.model.User;

public interface UserService extends UserDetailsService {
	void saveUser(User user);
	User findByEmail(String email);
}
