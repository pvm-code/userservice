package com.userservice.metrics;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class MetricsService {
	
	private final Counter userRegistrations;
	private final Counter loginSuccess;
	private final Counter loginFailure;
	private final Counter jwtValidationFailure;
	private final Counter kafkaProduced;
	
	public MetricsService(MeterRegistry registry) {
		
		this.userRegistrations=
				registry.counter("user_registrations_total");
	
		this.loginSuccess=
				registry.counter("user_login_success_total");
		
		this.loginFailure=
				registry.counter("user_logins_failed_total");
		
		this.jwtValidationFailure=
				registry.counter("jwt_validation_failed_total");
		
		this.kafkaProduced=
				registry.counter("kafka_messages_produced_total");
		
	}
	

    public void incrementRegistration() {
        userRegistrations.increment();
    }

    public void incrementLoginSuccess() {
        loginSuccess.increment();
    }

    public void incrementLoginFailure() {
        loginFailure.increment();
    }

    public void incrementJwtFailure() {
        jwtValidationFailure.increment();
    }

    public void incrementKafkaProduced() {
        kafkaProduced.increment();
    }
	

}
