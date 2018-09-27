package no.itminds.movies.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4930206085581618536L;
	private Long resourceId;
	private LocalDateTime requestedCreated;
	private String className;
	
	public NotFoundException() {
		super();
		requestedCreated = LocalDateTime.now();
	}
	public NotFoundException(String message, Throwable cause, Long resourceId, String className) {
		super(message, cause);
		this.resourceId = resourceId;
		this.className = className;
		requestedCreated = LocalDateTime.now();
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public LocalDateTime getRequestedCreated() {
		return requestedCreated;
	}
	public void setRequestedCreated(LocalDateTime requestedCreated) {
		this.requestedCreated = requestedCreated;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
}
