package org.example.spring_kafka.modelAssembler;

import org.example.spring_kafka.dto.AccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AccountModelAssemblerTest {

    @InjectMocks
    private AccountModelAssembler accountModelAssembler;

    @Test
    void toModel_ShouldAddLinks() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setTitle("Test Account");
        accountDTO.setUserId(1L);
        accountDTO.setUserFirstName("Max");
        accountDTO.setUserLastName("Mad");
        accountDTO.setUserEmail("max@mail.com");
        accountDTO.setCreatedAt(LocalDate.now());
        accountDTO.setUpdatedAt(LocalDate.now());

        EntityModel<AccountDTO> model = accountModelAssembler.toModel(accountDTO);

        assertNotNull(model);
        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("all-accounts"));
        assertTrue(model.hasLink("delete"));

        assertNotNull(model.getRequiredLink("self").getHref());
        assertNotNull(model.getRequiredLink("all-accounts").getHref());
        assertNotNull(model.getRequiredLink("delete").getHref());

        String selfLink = model.getRequiredLink("self").getHref();
        assertTrue(selfLink.contains("/accounts/1"));
    }
}