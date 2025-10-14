package org.example.spring_kafka;

import java.util.Locale;

import net.datafaker.Faker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaApplication.class, args);
	}

    @Bean
    public Faker getFaker() {
        return new Faker(new Locale("en", "US"));
    }
}
