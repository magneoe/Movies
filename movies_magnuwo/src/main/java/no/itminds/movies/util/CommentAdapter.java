package no.itminds.movies.util;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import no.itminds.movies.model.Comment;
import no.itminds.movies.model.dto.CommentDTO;
import no.itminds.movies.model.login.User;

public class CommentAdapter {
	private final static Logger logger = Logger.getLogger(CommentAdapter.class);
	
	public static Comment fromCommentDTO(CommentDTO commentDTO, User author, Date created) {
		Comment comment = new Comment();
		
		comment.setAuthor(author);
		comment.setComment(commentDTO.getComment());
		comment.setCreated(new Timestamp(created.getTime()));
		comment.setId(commentDTO.getMovieId());
		comment.setTitle(commentDTO.getTitle());
		
		return comment;
	}
}
