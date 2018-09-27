package no.itminds.movies.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import no.itminds.movies.exceptions.NotFoundException;

@ControllerAdvice
public class GlobalErrorHandler {

	private static Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);
	
	public final static String DEFAULT_ERROR_VIEW = "error";
	public final static String RESOURCE_NOT_FOUND_VIEW = "notfound";
	
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFoundException(HttpServletRequest request, Exception ex) {
		final String errorMessage = ex.getMessage();
		logger.debug(ex.getMessage(), ex.getStackTrace().toString());

		ModelAndView modelAndView = new ModelAndView(RESOURCE_NOT_FOUND_VIEW);
		modelAndView.addObject("errorMessage", errorMessage);
		modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
		return modelAndView;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleGeneralException(HttpServletRequest request, Exception ex) {
		final String errorMessage = ex.getMessage();
		logger.debug(ex.getMessage(), ex.getStackTrace().toString());

		ModelAndView modelAndView = new ModelAndView(DEFAULT_ERROR_VIEW);
		modelAndView.addObject("errorMessage", errorMessage);
		return modelAndView;
	}
}
