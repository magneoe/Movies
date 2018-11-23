package no.itminds.movies.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import no.itminds.movies.service.UserService;
import no.itminds.movies.service.impl.TokenAuthenticationService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	@Autowired
	private UserService userDetailsService;
	
	@Autowired
	private TokenAuthenticationService tokenAuthService;
	
	protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
		authorizeRequests().
		antMatchers(
				"/",
				//"/static/**",
				//"/public/**",
				"/console/**", 
				"/api/movies/getAll", 
				"/api/movies/comments",
				"/api/genres/getAll",
				"/api/movies/{\\d+}",
				"/api/actors/getAll",
				"/api/movies/comments?{*}",
				"/createUser"
				).
		permitAll().
		antMatchers(HttpMethod.POST, "/login").permitAll().
		antMatchers("/api/movies/addMovie").
		hasRole("ADMIN").
		anyRequest().authenticated().
		and().
			addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), tokenAuthService), 
					UsernamePasswordAuthenticationFilter.class).
			addFilterBefore(new JWTAuthenticationFilter(tokenAuthService),
	                UsernamePasswordAuthenticationFilter.class).
			exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
		
		logger.debug(http.toString());
		//Enable h2 login
		http.headers().frameOptions().disable();
		http.csrf().disable();
		http.cors().disable();
	}

	@Bean(name="encoder")
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
	
//	@Bean
//	  TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
//	    final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
//	    filter.setAuthenticationManager(authenticationManager());
//	    filter.setAuthenticationSuccessHandler(successHandler());
//	    return filter;
//	  }
//
//	  @Bean
//	  SimpleUrlAuthenticationSuccessHandler successHandler() {
//	    final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
//	    successHandler.setRedirectStrategy(new NoRedirectStrategy());
//	    return successHandler;
//	  }
//
//	  /**
//	   * Disable Spring boot automatic filter registration.
//	   */
//	  @Bean
//	  FilterRegistrationBean disableAutoRegistration(final TokenAuthenticationFilter filter) {
//	    final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
//	    registration.setEnabled(false);
//	    return registration;
//	  }
//
//	  @Bean
//	  AuthenticationEntryPoint forbiddenEntryPoint() {
//	    return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
//	  }
//	}
}
