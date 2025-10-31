package org.example.spring_kafka.service;

import jakarta.transaction.Transactional;

import org.example.spring_kafka.dto.UserCreateDTO;
import org.example.spring_kafka.dto.UserDTO;
import org.example.spring_kafka.dto.UserUpdateDTO;
import org.example.spring_kafka.exception.ResourceNotFoundException;
import org.example.spring_kafka.event.NotificationEvent;
import org.example.spring_kafka.mapper.UserMapper;
import org.example.spring_kafka.model.User;
import org.example.spring_kafka.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.spring_kafka.event.OperationType.USER_CREATED;
import static org.example.spring_kafka.event.OperationType.USER_DELETED;
import static org.example.spring_kafka.event.OperationType.USER_UPDATED;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private final String TOPIC = "user.notifications";

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        return userMapper.map(user);
    }

    @Transactional
    public UserDTO create(UserCreateDTO data) {
        User user = userMapper.map(data);
        userRepository.save(user);

        kafkaTemplate.send(TOPIC, new NotificationEvent(USER_CREATED, user.getEmail()));

        return userMapper.map(user);
    }

    @Transactional
    public UserDTO update(UserUpdateDTO data, long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        userMapper.update(data, user);
        userRepository.save(user);

        kafkaTemplate.send(TOPIC, new NotificationEvent(USER_UPDATED, user.getEmail()));

        return userMapper.map(user);
    }

    @Transactional
    public void delete(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        String email = user.getEmail();

        userRepository.deleteById(id);

        kafkaTemplate.send(TOPIC, new NotificationEvent(USER_DELETED, email));
    }
}
