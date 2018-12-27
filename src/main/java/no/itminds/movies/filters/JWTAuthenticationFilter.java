package no.itminds.movies.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import no.itminds.movies.service.impl.TokenAuthenticationService;


public class JWTAuthenticationFilter extends GenericFilterBean {

	private TokenAuthenticationService tokenAuthenticationService;
	
	public JWTAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
		this.tokenAuthenticationService = tokenAuthenticationService;
	}
	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain)
			throws IOException, ServletException {
		
		Authentication authentication = tokenAuthenticationService
		        .getAuthentication((HttpServletRequest)request);
			
			if(authentication != null)
				SecurityContextHolder.getContext()
		        	.setAuthentication(authentication);
			else
				SecurityContextHolder.clearContext();
		    chain.doFilter(request, response);
	}
	
}
