package org.example.spring_kafka.service;

import org.example.spring_kafka.dto.AccountCreateDTO;
import org.example.spring_kafka.dto.AccountDTO;
import org.example.spring_kafka.dto.AccountUpdateDTO;
import org.example.spring_kafka.exception.ResourceNotFoundException;
import org.example.spring_kafka.exception.UserNotFoundException;
import org.example.spring_kafka.mapper.AccountMapper;
import org.example.spring_kafka.model.Account;
import org.example.spring_kafka.model.User;
import org.example.spring_kafka.repository.AccountRepository;
import org.example.spring_kafka.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    private Account createTestAccount() {
        Account account = new Account();
        account.setTitle("Test Account");

        User user = new User();
        user.setFirstName("Max");
        user.setLastName("Mad");
        user.setEmail("max@mail.com");
        account.setUser(user);

        return account;
    }

    private AccountDTO createTestAccountDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setTitle("Test Account");
        accountDTO.setUserId(1L);
        accountDTO.setUserFirstName("Max");
        accountDTO.setUserLastName("Mad");
        accountDTO.setUserEmail("max@mail.com");
        accountDTO.setCreatedAt(LocalDate.now());
        accountDTO.setUpdatedAt(LocalDate.now());

        return accountDTO;
    }

    private AccountCreateDTO createTestAccountCreateDTO() {
        AccountCreateDTO createDTO = new AccountCreateDTO();
        createDTO.setUserId(1L);
        createDTO.setTitle("Test Account");

        return createDTO;
    }

    @Test
    void getAll_ShouldReturnAllAccounts() {
        Account account = createTestAccount();
        AccountDTO accountDTO = createTestAccountDTO();

        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(accountMapper.map(any(Account.class))).thenReturn(accountDTO);

        var result = accountService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Account", result.getFirst().getTitle());
        verify(accountRepository).findAll();
    }

    @Test
    void findById_WhenAccountExists_ShouldReturnAccount() {
        Long accountId = 1L;
        Account account = createTestAccount();
        AccountDTO accountDTO = createTestAccountDTO();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.map(any(Account.class))).thenReturn(accountDTO);

        var result = accountService.findById(accountId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(accountRepository).findById(accountId);
    }

    @Test
    void findById_WhenAccountNotExists_ShouldThrowException() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.findById(accountId));
    }

    @Test
    void create_WhenUserExists_ShouldCreateAccount() {
        AccountCreateDTO createDTO = createTestAccountCreateDTO();
        Account account = createTestAccount();
        AccountDTO accountDTO = createTestAccountDTO();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(accountMapper.map(createDTO)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.map(any(Account.class))).thenReturn(accountDTO);

        var result = accountService.create(createDTO);

        assertNotNull(result);
        assertEquals("Test Account", result.getTitle());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void create_WhenUserNotExists_ShouldThrowException() {
        AccountCreateDTO createDTO = createTestAccountCreateDTO();
        createDTO.setUserId(999L);

        when(userRepository.existsById(999L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> accountService.create(createDTO));
    }

    @Test
    void update_WhenAccountExists_ShouldUpdateAccount() {
        long accountId = 1L;
        AccountUpdateDTO updateDTO = new AccountUpdateDTO();
        updateDTO.setTitle(org.openapitools.jackson.nullable.JsonNullable.of("Updated Account"));

        Account existingAccount = createTestAccount();
        AccountDTO accountDTO = createTestAccountDTO();
        accountDTO.setTitle("Updated Account");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);
        when(accountMapper.map(any(Account.class))).thenReturn(accountDTO);

        var result = accountService.update(updateDTO, accountId);

        assertNotNull(result);
        assertEquals("Updated Account", result.getTitle());
        verify(accountMapper).update(eq(updateDTO), eq(existingAccount));
        verify(accountRepository).save(existingAccount);
    }

    @Test
    void update_WhenAccountNotExists_ShouldThrowException() {
        long accountId = 1L;
        AccountUpdateDTO updateDTO = new AccountUpdateDTO();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.update(updateDTO, accountId));
    }

    @Test
    void delete_WhenAccountExists_ShouldDeleteAccount() {
        Long accountId = 1L;
        doNothing().when(accountRepository).deleteById(accountId);

        accountService.delete(accountId);

        verify(accountRepository).deleteById(accountId);
    }
}