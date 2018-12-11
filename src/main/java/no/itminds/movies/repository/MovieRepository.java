package no.itminds.movies.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import no.itminds.movies.model.Comment;
import no.itminds.movies.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	Movie findByTitle(String title);
	
	@Query(value="SELECT c FROM Movie m INNER JOIN m.comments c WHERE m.id = ?1 ORDER BY c.comment.created DESC", 
			countQuery="SELECT count(c) FROM Movie m INNER JOIN m.comments c WHERE m.id = ?1")
	Page<Comment> getComments(Long movieId, Pageable pageable);
}


