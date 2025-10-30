package org.example.spring_kafka.controller;

import org.example.spring_kafka.AbstractIntegrationTest;
import org.example.spring_kafka.dto.AccountCreateDTO;
import org.example.spring_kafka.dto.UserCreateDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private long userId;

    @BeforeEach
    void setupUser(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("TRUNCATE TABLE accounts, users RESTART IDENTITY CASCADE");

        UserCreateDTO dto = new UserCreateDTO();
        dto.setFirstName("John");
        dto.setLastName("Wick");
        dto.setEmail("john.wick@mail.com");

        ResponseEntity<String> response = restTemplate.postForEntity("/users", dto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        userId = 1;
    }

    @Test
    void testCreateAccount() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setUserId(userId);
        dto.setTitle("Main account");

        ResponseEntity<String> response = restTemplate.postForEntity("/accounts", dto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Main account");
    }

    @Test
    void testGetAccountNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/accounts/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
