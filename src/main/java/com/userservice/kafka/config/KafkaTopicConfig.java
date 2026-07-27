package com.userservice.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

	
	public static final String USER_REGISTERED_TOPIC = "user-registered";
	
	@Bean
	public NewTopic userRegisteredTopic() {
		
		return new NewTopic(
				USER_REGISTERED_TOPIC,
				1,
				(short) 1
				
				
				);
	}
}
