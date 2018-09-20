package no.itminds.movies.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import no.itminds.movies.model.login.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email) throws UsernameNotFoundException;
}
