package no.itminds.movies.service.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import no.itminds.movies.model.Role;
import no.itminds.movies.model.User;
import no.itminds.movies.model.UserPrincipal;
import no.itminds.movies.repository.RoleRepository;
import no.itminds.movies.repository.UserRepository;
import no.itminds.movies.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Override
	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setActive(true);
		
		Role userRole = null;
		try {
			userRole = roleRepository.findByRole("ADMIN");
			user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userRepository.saveAndFlush(user);
		} catch (RoleNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new UserPrincipal(userRepository.findByEmail(username));
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	/*
	 * Private helper methods
	 */
	
	private void saveDefaultTestuser() {
		User newUser = new User();
		newUser.setEmail("magneoe@gmail.com");
		newUser.setPassword("password");
		newUser.setFailedLoginAttempts(0);
		newUser.setActive(true);
		newUser.setName("Magnus");
		newUser.setLastname("Østeng");
		
		saveUser(newUser);
	}

}
