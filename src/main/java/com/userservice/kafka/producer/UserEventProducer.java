package com.userservice.kafka.producer;

import com.userservice.kafka.config.KafkaTopicConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.userservice.kafka.event.UserRegisteredEvent;

@Service
public class UserEventProducer {

	
	private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

	public UserEventProducer(KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void publishUserRegistered(UserRegisteredEvent event) {
		
		
		kafkaTemplate.send(
				
				KafkaTopicConfig.USER_REGISTERED_TOPIC,
				event.getId().toString(),
				event
				
				
				);
		
		
	}
	
}
