package org.example.spring_kafka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.example.spring_kafka.dto.AccountCreateDTO;
import org.example.spring_kafka.dto.AccountDTO;
import org.example.spring_kafka.dto.AccountUpdateDTO;
import org.example.spring_kafka.modelAssembler.AccountModelAssembler;
import org.example.spring_kafka.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Operations related to account management (HATEOAS-enabled)")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountModelAssembler assembler;

    @Operation(
            summary = "Get all accounts",
            description = "Retrieve a list of all accounts in the system, with HATEOAS links included."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Accounts retrieved successfully",
            content = @Content(schema = @Schema(implementation = AccountDTO.class))
    )
    @GetMapping
    public CollectionModel<EntityModel<AccountDTO>> index() {
        var accounts = accountService.getAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(accounts,
                linkTo(methodOn(AccountController.class).index()).withSelfRel());
    }

    @Operation(
            summary = "Get account by ID",
            description = "Retrieve detailed information about a specific account by its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account found", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public EntityModel<AccountDTO> show(
            @Parameter(description = "Account ID", example = "1001") @PathVariable long id) {
        var account = accountService.findById(id);

        return assembler.toModel(account);
    }

    @Operation(
            summary = "Create a new account",
            description = "Create a new account for a specific user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<AccountDTO> create(
            @Valid @RequestBody AccountCreateDTO data) {
        var created = accountService.create(data);

        return assembler.toModel(created);
    }

    @Operation(
            summary = "Update account details",
            description = "Update an existing account by ID. Supports partial updates using nullable fields."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping("/{id}")
    public EntityModel<AccountDTO> update(
            @Valid @RequestBody AccountUpdateDTO data,
            @Parameter(description = "Account ID", example = "1001") @PathVariable long id) {
        var updated = accountService.update(data, id);

        return assembler.toModel(updated);
    }

    @Operation(
            summary = "Delete account",
            description = "Delete an account by its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "Account ID", example = "1001") @PathVariable long id) {
        accountService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
