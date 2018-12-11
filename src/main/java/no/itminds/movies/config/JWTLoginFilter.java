package no.itminds.movies.config;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.itminds.movies.model.login.User;
import no.itminds.movies.service.impl.TokenAuthenticationService;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{

	private TokenAuthenticationService tokenAuthService;
	
	public JWTLoginFilter(String url, AuthenticationManager authManager, TokenAuthenticationService tokenAuthService) {
		super(new AntPathRequestMatcher(url));
	    setAuthenticationManager(authManager);
		this.tokenAuthService = tokenAuthService;
	}
 
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		User user = new ObjectMapper()
		        .readValue(request.getInputStream(), User.class);
		    return getAuthenticationManager().authenticate(
		        new UsernamePasswordAuthenticationToken(
		            user.getEmail(),
		            user.getPassword(),
		            Collections.emptyList()
		        )
		    );
	}
	
	 @Override
	  protected void successfulAuthentication(
	      HttpServletRequest req,
	      HttpServletResponse res, FilterChain chain,
	      Authentication auth) throws IOException, ServletException {
	    tokenAuthService
	        .addAuthentication(res, auth.getName());
	  }
	
}
