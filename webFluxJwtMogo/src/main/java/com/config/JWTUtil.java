package com.config;


import com.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ard333
 */
@Component
public class JWTUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private final long expirationTime=43200;
    private final String secret="ThisIsSecretFor4JWTHS512SignatureAlgorithmThatMUSTHave512bitsKeySize";
	
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(token).getBody();
	}
	
	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(Date.from(Instant.now()));
	}
	
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRoles());
		return doGenerateToken(claims, user.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String username) {
		Instant now=Instant.now();
		return "Bearer "+Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plusSeconds(expirationTime)))
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
				.compact();
	}
	
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

}
