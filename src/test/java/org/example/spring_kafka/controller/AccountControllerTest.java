package org.example.spring_kafka.controller;

import org.example.spring_kafka.AbstractIntegrationTest;
import org.example.spring_kafka.dto.AccountCreateDTO;
import org.example.spring_kafka.dto.UserCreateDTO;
import org.example.spring_kafka.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class AccountControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Long userId;

    @BeforeEach
    void setUp() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("Account");
        userCreateDTO.setLastName("Owner");
        userCreateDTO.setEmail("account.owner@example.com");

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        userId = objectMapper.readTree(response).path("content").path("id").asLong();
    }

    @Test
    void shouldCreateAccount() throws Exception {
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setUserId(userId);
        accountCreateDTO.setTitle("Savings Account");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content.title").value("Savings Account"))
                .andExpect(jsonPath("$.content.userId").value(userId))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.all-accounts").exists());
    }

    @Test
    void shouldGetAccountById() throws Exception {
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setUserId(userId);
        accountCreateDTO.setTitle("Checking Account");

        String response = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long accountId = objectMapper.readTree(response).path("content").path("id").asLong();

        mockMvc.perform(get("/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.id").value(accountId))
                .andExpect(jsonPath("$.content.title").value("Checking Account"))
                .andExpect(jsonPath("$.content.userId").value(userId))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldGetAllAccounts() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._embedded.accountDTOList").isArray());
    }

    @Test
    void shouldReturnNotFoundForNonExistentAccount() throws Exception {
        mockMvc.perform(get("/accounts/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateAccountCreation() throws Exception {
        AccountCreateDTO invalidAccount = new AccountCreateDTO();

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundForNonExistentUserWhenCreatingAccount() throws Exception {
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setUserId(999L); // Non-existent user
        accountCreateDTO.setTitle("Test Account");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateDTO)))
                .andExpect(status().isNotFound());
    }
}
