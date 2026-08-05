package com.userservice.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.kafka.config.KafkaTopicConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.userservice.kafka.event.UserRegisteredEvent;
import com.userservice.metrics.MetricsService;

@Service
public class UserEventProducer {

	
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	private final ObjectMapper objectMapper;
	
	private final MetricsService metricsService;


	
	
	public UserEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper,MetricsService metricsService) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
		this.metricsService=metricsService;

	}
	public void publishUserRegistered(UserRegisteredEvent event) {

	    try {

	        String json = objectMapper.writeValueAsString(event);

	        kafkaTemplate.send(
	                KafkaTopicConfig.USER_REGISTERED_TOPIC,
	                event.getId().toString(),
	                json
	        ).whenComplete((result, ex) -> {

	            if (ex == null) {

	                System.out.println("Kafka message published successfully");

	                metricsService.incrementKafkaProduced();

	            } else {

	                System.err.println("Kafka publish failed");
	                ex.printStackTrace();

	            }

	        });

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to publish event", e);
	    }
	}
	
}
