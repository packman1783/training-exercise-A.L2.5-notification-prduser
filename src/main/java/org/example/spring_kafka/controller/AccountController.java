package org.example.spring_kafka.controller;

import org.example.spring_kafka.dto.AccountCreateDTO;
import org.example.spring_kafka.dto.AccountDTO;
import org.example.spring_kafka.dto.AccountUpdateDTO;
import org.example.spring_kafka.modelAssembler.AccountModelAssembler;
import org.example.spring_kafka.service.AccountService;

import jakarta.validation.Valid;

import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Operations related to accounts (HATEOAS-enabled)")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<AccountDTO>> index() {
        var accounts = accountService.getAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(accounts,
                linkTo(methodOn(AccountController.class).index()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<AccountDTO> show(@PathVariable long id) {
        var account = accountService.findById(id);

        return assembler.toModel(account);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<AccountDTO> create(@Valid @RequestBody AccountCreateDTO data) {
        var created = accountService.create(data);

        return assembler.toModel(created);
    }

    @PutMapping("/{id}")
    public EntityModel<AccountDTO> update(@Valid @RequestBody AccountUpdateDTO data, @PathVariable long id) {
        var updated = accountService.update(data, id);

        return assembler.toModel(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
