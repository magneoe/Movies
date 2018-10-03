package no.itminds.movies.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import no.itminds.movies.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	@Autowired
	private UserService userDetailsService;
	
	@Autowired
	private CustomAuthenticationSuccessHandler restApiSuccessHandler;
	
	@Autowired
	private SimpleUrlAuthenticationFailureHandler restApiFailureHandler;
	
	protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
		authorizeRequests().
		antMatchers(
				"/",
				"/static/**",
				"/login",
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
		antMatchers("/api/movies/addMovie").
		hasRole("ADMIN").
		anyRequest().authenticated().
		and().
		formLogin().
		successHandler(restApiSuccessHandler).
		failureHandler(restApiFailureHandler).
		permitAll().
		and().
		logout().
		permitAll();
		
		logger.debug(http.toString());
		//Enable h2 login
		http.csrf().disable();
		http.headers().frameOptions().disable();

	}

	@Bean(name="encoder")
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
	@Bean
    public CustomAuthenticationSuccessHandler restApiSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }
    @Bean
    public SimpleUrlAuthenticationFailureHandler restApiFailureHandler(){
        return new SimpleUrlAuthenticationFailureHandler();
    }
}
