package org.example.spring_kafka.config;

import org.example.spring_kafka.event.NotificationEvent;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;
import java.util.HashMap;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, NotificationEvent> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties());
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, NotificationEvent> kafkaTemplate(ProducerFactory<String, NotificationEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}

