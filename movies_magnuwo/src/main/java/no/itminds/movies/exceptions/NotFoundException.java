package no.itminds.movies.exceptions;

import java.time.LocalDateTime;

public class NotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4930206085581618536L;
	private Long resourceId;
	private LocalDateTime timeOfDBQuery;
	
	public NotFoundException() {
		super();
		timeOfDBQuery = LocalDateTime.now();
	}
	public NotFoundException(String message, Throwable cause, Long resourceId) {
		super(message, cause);
		this.resourceId = resourceId;
		timeOfDBQuery = LocalDateTime.now();
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public LocalDateTime getTimeOfDBQuery() {
		return timeOfDBQuery;
	}
	public void setTimeOfDBQuery(LocalDateTime timeOfDBQuery) {
		this.timeOfDBQuery = timeOfDBQuery;
	}
}
