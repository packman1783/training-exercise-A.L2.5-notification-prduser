package org.example.spring_kafka.config;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic userNotificationTopic() {
        return new NewTopic("user.notifications", 1, (short) 1);
    }
}
