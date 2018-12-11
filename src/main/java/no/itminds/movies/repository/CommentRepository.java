package no.itminds.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.itminds.movies.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
}
