package com.userservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.userservice.dto.request.LoginRequest;
import com.userservice.dto.response.LoginResponse;
import com.userservice.metrics.MetricsService;
import com.userservice.security.CustomUserDetails;
import com.userservice.security.JwtService;

@Service
public class AuthService {
	
	private final AuthenticationManager authenticationManager;
	
	private JwtService jwtService;
	
	private final MetricsService metricsService;


	public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,MetricsService metricsService) {
	
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.metricsService=metricsService;

	}
	
	
	public LoginResponse login(LoginRequest request) {

	    try {

	        Authentication authentication =
	                authenticationManager.authenticate(
	                        new UsernamePasswordAuthenticationToken(
	                                request.getEmail(),
	                                request.getPassword()
	                        )
	                );

	        CustomUserDetails userDetails =
	                (CustomUserDetails) authentication.getPrincipal();

	        String token = jwtService.generateToken(userDetails);

	        metricsService.incrementLoginSuccess();

	        return new LoginResponse(token);

	    } catch (AuthenticationException e) {
	        metricsService.incrementLoginFailure();
	        throw e;
	    }
	}
	

}
