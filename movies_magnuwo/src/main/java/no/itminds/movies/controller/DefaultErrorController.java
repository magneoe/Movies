package no.itminds.movies.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

//@Controller
//public class DefaultErrorController implements ErrorController {
//
//	private final static String DEFAULT_ERROR_PATH = "error";
//	
//	@Autowired
//    private ErrorAttributes errorAttributes;
//	
//	@Override
//	public String getErrorPath() {
//		return DEFAULT_ERROR_PATH;
//	}
//	
//	@GetMapping(value=DEFAULT_ERROR_PATH)
//    public ModelAndView error(ModelMap model) {
//		
//        return new ModelAndView(DEFAULT_ERROR_PATH, model);
//    }
//	
//	@GetMapping("/notfound")
//	public String notfound(HttpServletRequest request, HttpServletResponse response) {
//	    //return new ModelAndView("notfound", model);
//		return "notfound";
//	}
//}
