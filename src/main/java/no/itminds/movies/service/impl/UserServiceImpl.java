package no.itminds.movies.service.impl;

import java.time.LocalDateTime;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import no.itminds.movies.model.login.Role;
import no.itminds.movies.model.login.User;
import no.itminds.movies.model.login.UserPrincipal;
import no.itminds.movies.repository.RoleRepository;
import no.itminds.movies.repository.UserRepository;
import no.itminds.movies.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void saveUser(User user) throws PersistenceException {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setActive(true);
			user.setCreated(LocalDateTime.now());

			Role userRole = roleRepository.findByRole("ROLE_USER");
			user.addRole(userRole);
			userRepository.saveAndFlush(user);
		} catch (NullPointerException npEx) {
			npEx.printStackTrace();
			throw new PersistenceException("Could not operate on a User object being null");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		return new UserPrincipal(userRepository.findByEmail(username));
	}

	@Override
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null)
		{
			logger.debug("FindByEmail: no user exists with email: " + email);
		}
		return user;
	}

}
