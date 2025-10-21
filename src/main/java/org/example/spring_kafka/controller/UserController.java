package org.example.spring_kafka.controller;

import org.example.spring_kafka.dto.UserCreateDTO;
import org.example.spring_kafka.dto.UserDTO;

import jakarta.validation.Valid;

import java.util.List;

import org.example.spring_kafka.dto.UserUpdateDTO;
import org.example.spring_kafka.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User Operations")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get list of users")
    @ApiResponse(responseCode = "200", description = "List received successfully")
    @GetMapping
    public List<UserDTO> index() {
        return userService.getAll();
    }

    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public UserDTO show(@PathVariable long id) {
        return userService.findById(id);
    }

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO data) {
        return userService.create(data);
    }

    @Operation(summary = "Update user details")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping("/{id}")
    public UserDTO update(@Valid @RequestBody UserUpdateDTO data, @PathVariable long id) {
        return userService.update(data, id);
    }

    @Operation(summary = "Delete user by ID")
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
