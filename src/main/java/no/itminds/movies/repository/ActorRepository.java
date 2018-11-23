package no.itminds.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.itminds.movies.model.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {

}
