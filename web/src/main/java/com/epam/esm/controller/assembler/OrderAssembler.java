package com.epam.esm.controller.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements SimpleRepresentationModelAssembler<OrderDto> {

    @Override
    public void addLinks(EntityModel<OrderDto> resource) {
        resource.add(linkTo(methodOn(OrderController.class)
                .findOrderById(resource.getContent().getOrderId())).withSelfRel());
        resource.add(linkTo(methodOn(OrderController.class)
                .findAll(null)).withRel("gift_certificates"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<OrderDto>> resources) {

    }
}
