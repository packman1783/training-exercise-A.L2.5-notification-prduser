package org.example.spring_kafka.controller;

import org.example.spring_kafka.AbstractIntegrationTest;
import org.example.spring_kafka.dto.UserCreateDTO;
import org.example.spring_kafka.dto.UserUpdateDTO;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateAndGetUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setFirstName("John");
        dto.setLastName("Wick");
        dto.setEmail("john.wick@mail.com");

        ResponseEntity<String> response = restTemplate.postForEntity("/users", dto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("john.wick@mail.com");

        ResponseEntity<String> allUsers = restTemplate.getForEntity("/users", String.class);
        assertThat(allUsers.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allUsers.getBody()).contains("John");
    }

    @Test
    void testUpdateUser() {
        UserCreateDTO create = new UserCreateDTO();
        create.setFirstName("Alice");
        create.setLastName("Cooper");
        create.setEmail("alice@mail.com");
        var created = restTemplate.postForEntity("/users", create, String.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        UserUpdateDTO update = new UserUpdateDTO();
        update.setFirstName(org.openapitools.jackson.nullable.JsonNullable.of("Alison"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserUpdateDTO> request = new HttpEntity<>(update, headers);

        ResponseEntity<String> response = restTemplate.exchange("/users/1", HttpMethod.PUT, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Alison");
    }

    @Test
    void testDeleteUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setFirstName("Bob");
        dto.setLastName("Marley");
        dto.setEmail("bob@example.com");
        restTemplate.postForEntity("/users", dto, String.class);

        restTemplate.delete("/users/1");

        ResponseEntity<String> response = restTemplate.getForEntity("/users/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
