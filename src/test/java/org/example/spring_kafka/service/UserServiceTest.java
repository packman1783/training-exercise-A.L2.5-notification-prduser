package org.example.spring_kafka.service;

import org.example.spring_kafka.dto.UserCreateDTO;
import org.example.spring_kafka.dto.UserDTO;
import org.example.spring_kafka.event.NotificationEvent;
import org.example.spring_kafka.mapper.UserMapper;
import org.example.spring_kafka.model.User;
import org.example.spring_kafka.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirstName("Max");
        testUser.setLastName("Mad");
        testUser.setEmail("mad.max@mail.com");

        testUserDTO = new UserDTO();
        testUserDTO.setFirstName("Max");
        testUserDTO.setLastName("Mad");
        testUserDTO.setEmail("mad.max@mail.com");
    }

    @Test
    void testCreate_shouldSaveUserAndSendKafkaEvent() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setFirstName("Max");
        dto.setLastName("Mad");
        dto.setEmail("mad.max@mail.com");

        when(userMapper.map(dto)).thenReturn(testUser);
        when(userMapper.map(testUser)).thenReturn(testUserDTO);

        var result = userService.create(dto);

        verify(userRepository).save(testUser);

        ArgumentCaptor<NotificationEvent> captor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(kafkaTemplate).send(eq("user.notifications"), captor.capture());

        NotificationEvent sentEvent = captor.getValue();

        assertThat(sentEvent.getEmail()).isEqualTo("mad.max@mail.com");
        assertThat(sentEvent.getOperation()).isEqualTo("USER_CREATED");

        assertThat(result.getFirstName()).isEqualTo("Max");

        verifyNoMoreInteractions(kafkaTemplate, userRepository, userMapper);
    }
}
