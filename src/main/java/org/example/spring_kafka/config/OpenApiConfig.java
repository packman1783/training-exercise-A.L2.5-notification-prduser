package org.example.spring_kafka.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Kafka User & Account API")
                        .version("1.0")
                        .description("""
                                REST API for managing users and accounts.
                                Supports sending events to the Kafka topic `user.notifications`.
                                """)
                        .contact(new Contact()
                                .name("Tea Pot Team")
                                .email("fake-email@mail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
