package com.userservice.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userservice.dto.request.RegisterRequest;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.kafka.event.UserRegisteredEvent;
import com.userservice.kafka.producer.UserEventProducer;
import com.userservice.metrics.MetricsService;
import com.userservice.repository.UserRepository;

@Service
public class UserService {
	
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder ;
	private final UserEventProducer userEventProducer;
	private final MetricsService metricsService;
	
	public UserService(UserRepository userRepository
			,PasswordEncoder passwordEncoder
			,UserEventProducer userEventProducer
			,MetricsService metricsService) {
		
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.userEventProducer=userEventProducer;
		this.metricsService=metricsService;
		
	}
	
	
	@Transactional
	public User register(RegisterRequest request) {
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("email already registred");
		}
		User user=new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		
		User savedUser=userRepository.save(user);
		userRepository.flush();
		metricsService.incrementRegistration();
		
		UserRegisteredEvent event=new UserRegisteredEvent(
				
				savedUser.getId(),
				savedUser.getEmail(),
				savedUser.getName(),
				savedUser.getCreatedAt()
				
				
				);
		System.out.println("Calling Kafka producer...");
		
		userEventProducer.publishUserRegistered(event);
		
		
		return savedUser;
				
		
	}
	

}
