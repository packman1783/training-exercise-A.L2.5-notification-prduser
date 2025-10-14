package org.example.spring_kafka.service;

import org.example.spring_kafka.dto.AccountCreateDTO;
import org.example.spring_kafka.dto.AccountDTO;
import org.example.spring_kafka.dto.AccountUpdateDTO;
import org.example.spring_kafka.exception.ResourceNotFoundException;
import org.example.spring_kafka.mapper.AccountMapper;
import org.example.spring_kafka.model.Account;
import org.example.spring_kafka.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public List<AccountDTO> getAll() {
        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(accountMapper::map)
                .toList();
    }

    public AccountDTO findById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + id + " not found"));

        return accountMapper.map(account);
    }

    public AccountDTO create(AccountCreateDTO data) {
        Account account = accountMapper.map(data);
        accountRepository.save(account);

        return accountMapper.map(account);
    }

    public AccountDTO update(AccountUpdateDTO data, long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + id + " not found"));

        accountMapper.update(data, account);
        accountRepository.save(account);

        return accountMapper.map(account);
    }

    public void delete(long id) {
        accountRepository.deleteById(id);
    }
}
