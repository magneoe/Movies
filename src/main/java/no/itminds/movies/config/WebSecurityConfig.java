package no.itminds.movies.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
	
	@Value("#{T(java.util.Arrays).asList('${ALLOWED_ORIGINS}')}")
	private List<String> allowedOrigins;

	protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/", "/console/**", "/api/movies/getAll", "/api/movies/comments", "/api/genres/getAll",
						"/api/movies/{\\d+}", "/api/actors/getAll", "/api/movies/comments?{*}", "/createUser")
				.permitAll().
				antMatchers(HttpMethod.POST, "/login/**").
				permitAll().antMatchers("/api/movies/addMovie")
				.hasRole("ADMIN").anyRequest().authenticated().and()
				.addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), tokenAuthService),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JWTAuthenticationFilter(tokenAuthService),
						UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		logger.debug(http.toString());
		http.httpBasic().disable();
		// Enable h2 login
		http.headers().frameOptions().disable();
		http.csrf().disable();
		http.cors();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowCredentials(true);
	    configuration.setAllowedOrigins(allowedOrigins);
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
	    configuration.setAllowedHeaders(Arrays.asList("X-Requested-With","Origin","Content-Type","Accept","Authorization"));

	    // This allow us to expose the headers
	    configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
	            "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/api/**", configuration);
	    source.registerCorsConfiguration("/login", configuration);
	    return source;
	}

	@Bean(name = "encoder")
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
}
