package org.example.spring_kafka.controller;

import org.example.spring_kafka.mapper.AccountMapper;
import org.example.spring_kafka.model.Account;
import org.example.spring_kafka.model.User;
import org.example.spring_kafka.repository.AccountRepository;
import org.example.spring_kafka.repository.UserRepository;
import org.example.spring_kafka.util.ModelGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

@AutoConfigureMockMvc
public class AccountControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private Account testAccount;

    private User anotherUser;

    @BeforeEach
    public void setUp() {
        var user = Instancio.of(modelGenerator.getUserModel()).create();
        anotherUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(user);
        userRepository.save(anotherUser);
        testAccount = Instancio.of(modelGenerator.getAccountModel()).create();
        testAccount.setUser(user);
    }

    @Test
    public void testIndex() throws Exception {
        accountRepository.save(testAccount);
        var result = mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        accountRepository.save(testAccount);

        var request = get("/accounts/{id}", testAccount.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testAccount.getTitle()),
                v -> v.node("userId").isEqualTo(testAccount.getUser().getId()),
                v -> v.node("userFirstName").isEqualTo(testAccount.getUser().getFirstName()),
                v -> v.node("userLastName").isEqualTo(testAccount.getUser().getLastName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = accountMapper.map(testAccount);

        var request = post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var account = accountRepository.findByTitle(testAccount.getTitle()).get();

        assertThat(account).isNotNull();
        assertThat(account.getTitle()).isEqualTo(testAccount.getTitle());
        assertThat(account.getUser().getId()).isEqualTo(testAccount.getUser().getId());
    }

    @Test
    public void testCreateWithWrongUser() throws Exception {
        var dto = accountMapper.map(testAccount);
        dto.setUserId(12345L);

        var request = post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdate() throws Exception {
        accountRepository.save(testAccount);

        var dto = accountMapper.map(testAccount);

        dto.setTitle("new title");
        dto.setUserId(anotherUser.getId());

        var request = put("/accounts/{id}", testAccount.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = accountRepository.findById(testAccount.getId()).get();

        assertThat(task.getTitle()).isEqualTo(dto.getTitle());
        assertThat(task.getUser().getId()).isEqualTo(dto.getUserId());
    }

    @Test
    public void testPartialUpdate() throws Exception {
        accountRepository.save(testAccount);

        var dto = new HashMap<String, Long>();
        dto.put("userId", anotherUser.getId());

        var request = put("/accounts/{id}", testAccount.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = accountRepository.findById(testAccount.getId()).get();

        assertThat(task.getTitle()).isEqualTo(testAccount.getTitle());
        assertThat(task.getUser().getId()).isEqualTo(dto.get("userId"));
    }

    @Test
    public void testDestroy() throws Exception {
        accountRepository.save(testAccount);
        var request = delete("/accounts/{id}", testAccount.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(accountRepository.existsById(testAccount.getId())).isEqualTo(false);
    }
}
