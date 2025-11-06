package org.example.spring_kafka.modelAssembler;

import org.example.spring_kafka.controller.UserController;
import org.example.spring_kafka.dto.UserDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(UserDTO user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).show(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).index()).withRel("all-users"),
                linkTo(methodOn(UserController.class).update(null, user.getId())).withRel("update"),
                linkTo(methodOn(UserController.class).delete(user.getId())).withRel("delete")
        );
    }
}
