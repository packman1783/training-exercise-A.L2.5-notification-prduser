package org.example.spring_kafka.service;

import org.example.spring_kafka.dto.UserCreateDTO;
import org.example.spring_kafka.dto.UserUpdateDTO;
import org.example.spring_kafka.dto.UserDTO;
import org.example.spring_kafka.event.NotificationEvent;
import org.example.spring_kafka.exception.ResourceNotFoundException;
import org.example.spring_kafka.mapper.UserMapper;
import org.example.spring_kafka.model.User;
import org.example.spring_kafka.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import static org.example.spring_kafka.event.OperationType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @InjectMocks
    private UserService userService;

    private User createTestUser() {
        User user = new User();
        user.setFirstName("Max");
        user.setLastName("Mad");
        user.setEmail("max@mail.com");

        return user;
    }

    private UserDTO createTestUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Max");
        userDTO.setLastName("Mad");
        userDTO.setEmail("max@mail.com");
        userDTO.setCreatedAt(LocalDate.now());

        return userDTO;
    }

    private UserCreateDTO createTestUserCreateDTO() {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setFirstName("Max");
        createDTO.setLastName("Mad");
        createDTO.setEmail("max@mail.com");

        return createDTO;
    }

    @Test
    void getAll_ShouldReturnAllUsers() {
        User user = createTestUser();
        UserDTO userDTO = createTestUserDTO();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.map(any(User.class))).thenReturn(userDTO);

        var result = userService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Max", result.getFirst().getFirstName());
        verify(userRepository).findAll();
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        Long userId = 1L;
        User user = createTestUser();
        UserDTO userDTO = createTestUserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.map(any(User.class))).thenReturn(userDTO);

        var result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_WhenUserNotExists_ShouldThrowException() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    void create_ShouldSaveUserAndSendEvent() {
        UserCreateDTO createDTO = createTestUserCreateDTO();
        User user = createTestUser();
        UserDTO userDTO = createTestUserDTO();

        when(userMapper.map(createDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.map(any(User.class))).thenReturn(userDTO);

        var result = userService.create(createDTO);

        assertNotNull(result);
        assertEquals("Max", result.getFirstName());
        verify(userRepository).save(any(User.class));
        verify(kafkaTemplate).send(eq("user.notifications"), any(NotificationEvent.class));
    }

    @Test
    void update_WhenUserExists_ShouldUpdateAndSendEvent() {
        long userId = 1L;
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setFirstName(org.openapitools.jackson.nullable.JsonNullable.of("Bob"));

        User existingUser = createTestUser();
        UserDTO userDTO = createTestUserDTO();
        userDTO.setFirstName("Bob");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.map(any(User.class))).thenReturn(userDTO);

        var result = userService.update(updateDTO, userId);

        assertNotNull(result);
        assertEquals("Bob", result.getFirstName());
        verify(userMapper).update(eq(updateDTO), eq(existingUser));
        verify(kafkaTemplate).send(eq("user.notifications"), any(NotificationEvent.class));
    }

    @Test
    void update_WhenUserNotExists_ShouldThrowException() {
        long userId = 1L;
        UserUpdateDTO updateDTO = new UserUpdateDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(updateDTO, userId));
    }

    @Test
    void delete_WhenUserExists_ShouldDeleteAndSendEvent() {
        Long userId = 1L;
        User user = createTestUser();
        user.setEmail("max@mail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
        verify(kafkaTemplate).send(eq("user.notifications"),
                argThat(event ->
                        event.getOperation() == USER_DELETED &&
                                event.getEmail().equals("max@mail.com")
                ));
    }

    @Test
    void delete_WhenUserNotExists_ShouldThrowException() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(userId));
    }
}