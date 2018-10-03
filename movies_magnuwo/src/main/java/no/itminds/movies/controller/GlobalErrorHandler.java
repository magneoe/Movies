package no.itminds.movies.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import no.itminds.movies.exceptions.NotFoundException;

@Order(value=1)
@RestControllerAdvice
public class GlobalErrorHandler {

	private static Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);
	
	@ExceptionHandler(value= MissingServletRequestParameterException.class)
	protected ResponseEntity<ErrorResponse> handleMissingParameterException(MissingServletRequestParameterException msrex, HttpServletRequest request){
		msrex.printStackTrace();
		logger.warn(msrex.getMessage());
		
		final String errorMessage = String.format("Missing parameter: " + msrex.getParameterName() + " of type: " + msrex.getParameterType());
		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI());

		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
	protected ResponseEntity<ErrorResponse> handleInvalidMethodArgument(RuntimeException rex, HttpServletRequest request){
		rex.printStackTrace();
		logger.warn(rex.getMessage());
		
		final String errorMessage = String.format("Invalid parameters provided:" + rex.getMessage());
		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI());

		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = { NotFoundException.class })
	protected ResponseEntity<ErrorResponse> handleConflict(NotFoundException nfex, HttpServletRequest request) {
		nfex.printStackTrace();
		logger.warn(nfex.getMessage());
		
		final String errorMessage = String.format("Unable to locate the requested resource: [%s] with id: %d", nfex.getClassName(), nfex.getResourceId());
		ErrorResponse error = new ErrorResponse(nfex.getRequestedCreated(), HttpStatus.NOT_FOUND.value(), errorMessage, request.getRequestURI());

		return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
		logger.debug(ex.getMessage());
		ex.printStackTrace();
		
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(), 
				HttpStatus.INTERNAL_SERVER_ERROR.value(), 
				"An error occured processing the request", 
				request.getRequestURI().toString());

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
private class ErrorResponse {
		
		private LocalDateTime timestamp;
		private int status;
		private String message;
		private String uri;
		
		public ErrorResponse(LocalDateTime timestamp, int status, String message, String uri) {
			this.timestamp = timestamp;
			this.status = status;
			this.message = message;
			this.uri = uri;
		}
		public LocalDateTime getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
	}
}
