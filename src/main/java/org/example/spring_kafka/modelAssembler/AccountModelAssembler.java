package org.example.spring_kafka.modelAssembler;

import org.example.spring_kafka.controller.AccountController;
import org.example.spring_kafka.dto.AccountDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class AccountModelAssembler implements RepresentationModelAssembler<AccountDTO, EntityModel<AccountDTO>> {
    @Override
    public EntityModel<AccountDTO> toModel(AccountDTO account) {
        return EntityModel.of(account,
                linkTo(methodOn(AccountController.class).show(account.getId())).withSelfRel(),
                linkTo(methodOn(AccountController.class).index()).withRel("all-accounts"),
                linkTo(methodOn(AccountController.class).delete(account.getId())).withRel("delete")
        );
    }
}
