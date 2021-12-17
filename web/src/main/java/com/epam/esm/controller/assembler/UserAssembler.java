package com.epam.esm.controller.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.UserEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements SimpleRepresentationModelAssembler<UserEntity> {
    @Override
    public void addLinks(EntityModel<UserEntity> resource) {
        resource.add(linkTo(methodOn(UserController.class).findById(resource.getContent().getUserId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).findOrdersByUserId(resource.getContent().getUserId())).withRel("orders"));
        resource.add(linkTo(methodOn(UserController.class).makeOrder(resource.getContent().getUserId(), null)).withRel("make_order"));
        resource.add(linkTo(methodOn(UserController.class).findAll(null)).withRel("find_all_user"));


    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserEntity>> resources) {

    }
}
