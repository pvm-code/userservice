package com.userservice.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.userservice.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	
	private final JwtProperties jwtProperties;

	public JwtService(JwtProperties jwtProperties) {
		super();
		this.jwtProperties = jwtProperties;
	}
	
	

//   this is method for getting secretkey from bunker and make ready for sign and verify token	
	private SecretKey getSigningKey() {
		
		
		return Keys.hmacShaKeyFor(
				
				jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
				);	
		
	}
	
	
	//method for generating token
	
	public String generateToken(CustomUserDetails userDetails) {
		
		Map<String, Object> claims = new HashMap<>();
		
		claims.put("role", userDetails.getRole().name());
		
		
		Date now = new Date();
		
		Date expiryDate = new Date(
				now.getTime() + jwtProperties.getExpiration()
				);
		
		
		return Jwts.builder()
				.addClaims(claims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();

	}
	
	
	
	// extract allclaims
	private Claims extractAllClaims(String token) {

	    return Jwts.parserBuilder()
	            .setSigningKey(getSigningKey())
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
	
	
	
	public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

	
	
	public Date extractExpiration(String token) {
		
		return extractAllClaims(token).getExpiration();
	}
	
	
	
	 private boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }
	
	 
	 
	 public boolean isTokenValid(String token,
             CustomUserDetails userDetails) {

		 	String username = extractUsername(token);

		 		return username.equals(userDetails.getUsername())
		 				&& !isTokenExpired(token);
}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
