package no.itminds.movies.controller;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import no.itminds.movies.exceptions.NotFoundException;


@RestControllerAdvice
@Order(value=1)
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { NotFoundException.class })
	protected ResponseEntity<ErrorResponse> handleConflict(NotFoundException nfex, WebRequest request) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(String.format("Unable to locate the requested resource: [%s] with id: %d", nfex.getClassName(), nfex.getResourceId()));
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
	}

	private class ErrorResponse {
		
		private int errorCode;
		private String message;
		
		public int getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(int errorCode) {
			this.errorCode = errorCode;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
