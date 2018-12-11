package no.itminds.movies.service.impl;

import static java.util.Collections.emptyList;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenAuthenticationService {
	static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);
	
	static final long EXPIRATIONTIME = 90000; //30 mins 
	private static final String SECRET = "ThisIsASecret";
	public static final String TOKEN_PREFIX = "Bearer";
	public static final String HEADER_STRING = "Authorization";

	public void addAuthentication(HttpServletResponse res, String username) {
		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token.
			String user = null;
			try {
				user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();
			}
			catch(MalformedJwtException mjwtex) {
				logger.warn("Unable to parse JWT");
			}
			catch(ExpiredJwtException exJwtEx) {
				logger.warn(exJwtEx.getMessage());
			}
			return user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;
		}
		return null;
	}
	
	public String getTokenForTesting(String username) {
		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		return JWT;
	}
}
