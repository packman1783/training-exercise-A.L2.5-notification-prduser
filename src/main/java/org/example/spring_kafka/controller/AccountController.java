package org.example.spring_kafka.controller;

import org.example.spring_kafka.dto.AccountCreateDTO;
import org.example.spring_kafka.dto.AccountDTO;
import org.example.spring_kafka.dto.AccountUpdateDTO;
import org.example.spring_kafka.service.AccountService;

import jakarta.validation.Valid;

import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Operations with user accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Operation(summary = "Get a list of accounts")
    @ApiResponse(responseCode = "200", description = "List received successfully")
    @GetMapping
    public List<AccountDTO> index() {
        return accountService.getAll();
    }

    @Operation(summary = "Get an account by ID")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/{id}")
    public AccountDTO show(@PathVariable long id) {
        return accountService.findById(id);
    }

    @Operation(summary = "Create a new account")
    @ApiResponse(responseCode = "201", description = "Account successfully created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO create(@Valid @RequestBody AccountCreateDTO data) {
        return accountService.create(data);
    }

    @Operation(summary = "Update account information")
    @ApiResponse(responseCode = "200", description = "Account updated successfully")
    @PutMapping("/{id}")
    public AccountDTO update(@Valid @RequestBody AccountUpdateDTO data, @PathVariable long id) {
        return accountService.update(data, id);
    }

    @Operation(summary = "Delete account by ID")
    @ApiResponse(responseCode = "204", description = "Account successfully deleted.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        accountService.delete(id);
    }

}
