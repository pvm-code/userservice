package com.userservice.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.kafka.config.KafkaTopicConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.userservice.kafka.event.UserRegisteredEvent;

@Service
public class UserEventProducer {

	
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	private final ObjectMapper objectMapper;

	
	
	public UserEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	public void publishUserRegistered(UserRegisteredEvent event) {
		
		try {
			String json = objectMapper.writeValueAsString(event);

			kafkaTemplate.send(
					
					KafkaTopicConfig.USER_REGISTERED_TOPIC,
					event.getId().toString(),
					json
					
					
					);
		} catch (JsonProcessingException e) {
		    e.printStackTrace();
		    throw new RuntimeException("failed to serialize event", e);
		}
		
	}
	
}
