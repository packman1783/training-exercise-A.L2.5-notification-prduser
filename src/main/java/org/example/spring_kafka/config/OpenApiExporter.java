package org.example.spring_kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.OpenAPI;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class OpenApiExporter {
    @Bean
    public ApplicationRunner exportOpenApi(OpenAPI openAPI) {
        return args -> {
            try {
                Path outputPath = Path.of("src/main/resources/static/openapi.json");

                ObjectMapper jsonMapper = new ObjectMapper();
                jsonMapper.findAndRegisterModules();

                jsonMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(outputPath.toFile(), openAPI);

                System.out.println("OpenAPI specification (JSON) exported to: " + outputPath.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("Error when exporting OpenAPI specification: " + e.getMessage());
            }
        };
    }
}