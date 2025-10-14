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

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<AccountDTO> index() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public AccountDTO show(@PathVariable long id) {
        return accountService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO create(@Valid @RequestBody AccountCreateDTO data) {
        return accountService.create(data);
    }

    @PutMapping("/{id}")
    public AccountDTO update(@Valid @RequestBody AccountUpdateDTO data, @PathVariable long id) {
        return accountService.update(data, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        accountService.delete(id);
    }

}
