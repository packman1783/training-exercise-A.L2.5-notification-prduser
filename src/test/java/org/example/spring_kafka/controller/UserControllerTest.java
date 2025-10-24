package org.example.spring_kafka.controller;

import org.example.spring_kafka.AbstractIntegrationTest;
import org.example.spring_kafka.dto.UserCreateDTO;
import org.example.spring_kafka.dto.UserUpdateDTO;
import org.example.spring_kafka.repository.UserRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class UserControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateUser() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("John");
        userCreateDTO.setLastName("Doe");
        userCreateDTO.setEmail("john.doe@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content.firstName").value("John"))
                .andExpect(jsonPath("$.content.lastName").value("Doe"))
                .andExpect(jsonPath("$.content.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.all-users").exists());
    }

    @Test
    void shouldGetUserById() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("Jane");
        userCreateDTO.setLastName("Smith");
        userCreateDTO.setEmail("jane.smith@example.com");

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(response).path("content").path("id").asLong();

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.id").value(userId))
                .andExpect(jsonPath("$.content.firstName").value("Jane"))
                .andExpect(jsonPath("$.content.lastName").value("Smith"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._embedded.userDTOList").isArray());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("Original");
        userCreateDTO.setLastName("Name");
        userCreateDTO.setEmail("original@example.com");

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(response).path("content").path("id").asLong();

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setFirstName(org.openapitools.jackson.nullable.JsonNullable.of("Updated"));
        userUpdateDTO.setLastName(org.openapitools.jackson.nullable.JsonNullable.of("Name"));

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.firstName").value("Updated"))
                .andExpect(jsonPath("$.content.lastName").value("Name"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("ToDelete");
        userCreateDTO.setLastName("User");
        userCreateDTO.setEmail("delete@example.com");

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(response).path("content").path("id").asLong();

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundForNonExistentUser() throws Exception {
        mockMvc.perform(get("/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateUserCreation() throws Exception {
        UserCreateDTO invalidUser = new UserCreateDTO();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }
}