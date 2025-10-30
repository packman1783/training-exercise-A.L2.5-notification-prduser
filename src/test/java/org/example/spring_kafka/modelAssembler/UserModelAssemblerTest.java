package org.example.spring_kafka.modelAssembler;

import org.example.spring_kafka.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;


@ExtendWith(MockitoExtension.class)
class UserModelAssemblerTest {

    @InjectMocks
    private UserModelAssembler userModelAssembler;

    @Test
    void toModel_ShouldAddLinks() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Max");
        userDTO.setLastName("Mad");
        userDTO.setEmail("max@mail.com");
        userDTO.setCreatedAt(LocalDate.now());

        EntityModel<UserDTO> model = userModelAssembler.toModel(userDTO);

        assertNotNull(model);
        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("all-users"));
        assertTrue(model.hasLink("delete"));

        assertNotNull(model.getRequiredLink("self").getHref());
        assertNotNull(model.getRequiredLink("all-users").getHref());
        assertNotNull(model.getRequiredLink("delete").getHref());

        String selfLink = model.getRequiredLink("self").getHref();
        assertTrue(selfLink.contains("/users/1"));
    }
}