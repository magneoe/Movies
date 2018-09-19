package no.itminds.movies.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import no.itminds.movies.model.User;
import no.itminds.movies.service.impl.UserServiceImpl;

@Controller
public class LoginController {

	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User existingUser = userService.findByEmail(auth.getName());
		if(existingUser != null) {
			return "/movies/index";
		}
		return "login";
	}
	
	@RequestMapping(value="/loginFailure", method=RequestMethod.POST)
	public ModelAndView loginFailed(ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("errorMessage", "Authentication failed: wrong username/password");
		return modelAndView;
	}
	
	@RequestMapping(value="/createUser", method=RequestMethod.GET)
	public String createUser(ModelMap modelMap) {
		return "createUser";
	}
	@RequestMapping(value="/createUser", method=RequestMethod.POST)
	public ModelAndView createUserSubmit(User user, String passwordRepeated, ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView("createUser", modelMap);
		List<String> formErrors = new ArrayList<>();
		
		//Check for violations
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		
		violations.stream().forEach(violation -> formErrors.add(violation.getMessage()));
		
		if(!passwordRepeated.equals(user.getPassword())) {
			formErrors.add("Passwords do not match!");
		}
		
		User userExists = userService.findByEmail(user.getEmail());
		if(userExists != null) {
			formErrors.add("There has already been registered an user with this email");
		}
		
		if(!formErrors.isEmpty()) {
			modelAndView.addObject("formErrors", formErrors);
		}
		else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "The user has successfully been created!");
			modelAndView.setViewName("login");
		}
		
		return modelAndView;
	}
}

