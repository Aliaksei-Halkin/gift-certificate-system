package com.epam.esm.controller.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagAssembler extends RepresentationModelAssemblerSupport<Tag, Tag> {

    public TagAssembler() {
        super(Tag.class, Tag.class);
    }

    @Override
    public Tag toModel(Tag entity) {
        entity.add(linkTo(methodOn(TagController.class).findTagById(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(TagController.class).deleteTagById(entity.getId())).withRel("delete"));
        return entity;
    }

    @Override
    public CollectionModel toCollectionModel(Iterable<? extends Tag> entities) {
        CollectionModel<Tag> tagModels = super.toCollectionModel(entities);
        tagModels.add(linkTo(methodOn(TagController.class).findAllTags(new HashMap<>())).withSelfRel());
        return tagModels;
    }
}
