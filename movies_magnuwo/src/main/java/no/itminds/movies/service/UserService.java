package no.itminds.movies.service;

import javax.persistence.PersistenceException;

import org.springframework.security.core.userdetails.UserDetailsService;

import no.itminds.movies.model.login.User;

public interface UserService extends UserDetailsService {
	void saveUser(User user) throws PersistenceException;
	User findByEmail(String email);
}
