package no.itminds.movies.repository;

import javax.management.relation.RoleNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.itminds.movies.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRole(String role) throws RoleNotFoundException;
}
