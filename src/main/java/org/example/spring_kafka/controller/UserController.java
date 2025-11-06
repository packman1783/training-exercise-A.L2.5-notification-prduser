package org.example.spring_kafka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.example.spring_kafka.dto.UserCreateDTO;
import org.example.spring_kafka.dto.UserDTO;
import org.example.spring_kafka.dto.UserUpdateDTO;
import org.example.spring_kafka.modelAssembler.UserModelAssembler;
import org.example.spring_kafka.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to user management (HATEOAS-enabled)")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserModelAssembler assembler;

    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users in the system, including HATEOAS links."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
    )
    @GetMapping
    public CollectionModel<EntityModel<UserDTO>> index() {
        var users = userService.getAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).index()).withSelfRel());
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve detailed information about a user by their unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public EntityModel<UserDTO> show(
            @Parameter(description = "User ID", example = "1") @PathVariable long id) {
        var user = userService.findById(id);

        return assembler.toModel(user);
    }

    @Operation(
            summary = "Create a new user",
            description = "Register a new user with the provided name and email address."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDTO> create(
            @Valid @RequestBody UserCreateDTO data) {
        var created = userService.create(data);

        return assembler.toModel(created);
    }

    @Operation(
            summary = "Update user details",
            description = "Update an existing user's name or email by ID. Supports partial updates."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public EntityModel<UserDTO> update(
            @Valid @RequestBody UserUpdateDTO data,
            @Parameter(description = "User ID", example = "1") @PathVariable long id) {
        var updated = userService.update(data, id);

        return assembler.toModel(updated);
    }

    @Operation(
            summary = "Delete user",
            description = "Delete a user from the system by their unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "User ID", example = "1") @PathVariable long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
