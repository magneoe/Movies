package no.itminds.movies.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import no.itminds.movies.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userDetailsService;
	
	
	protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
		authorizeRequests().
		antMatchers("/", "/movies/index", "/console/**", "/createUser").
		permitAll().
		anyRequest().authenticated().
		and().
		formLogin().
		loginPage("/login").
		defaultSuccessUrl("/movies/index").
		failureForwardUrl("/loginFailure").
		permitAll().
		and().
		logout().
		logoutUrl("/logout").
		logoutSuccessUrl("/").
		permitAll();
		
		
		//Enable h2 login
		http.csrf().disable();
		http.headers().frameOptions().disable();

	}

	@Bean(name="encoder")
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
}
