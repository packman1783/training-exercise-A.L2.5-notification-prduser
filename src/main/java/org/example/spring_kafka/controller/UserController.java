package org.example.spring_kafka.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to users (HATEOAS-enabled)")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<UserDTO>> index() {
        var users = userService.getAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).index()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UserDTO> show(@PathVariable long id) {
        var user = userService.findById(id);

        return assembler.toModel(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDTO> create(@Valid @RequestBody UserCreateDTO data) {
        var created = userService.create(data);

        return assembler.toModel(created);
    }

    @PutMapping("/{id}")
    public EntityModel<UserDTO> update(@Valid @RequestBody UserUpdateDTO data, @PathVariable long id) {
        var updated = userService.update(data, id);

        return assembler.toModel(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
