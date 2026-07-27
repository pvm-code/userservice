package com.userservice.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.userservice.kafka.config.KafkaTopicConfig;
import com.userservice.kafka.event.UserRegisteredEvent;

@Service
public class UserEventConsumer {
	
	
	private static final Logger log =
			LoggerFactory.getLogger(UserEventConsumer.class);
	
	
	@KafkaListener(
			
			
			topics = KafkaTopicConfig.USER_REGISTERED_TOPIC,
			groupId = "user-service-group"

			
			)
	
	public void consume(UserRegisteredEvent event) {
		
		log.info("User Registered Event Received : {} ",event);
		
	}
	

}
