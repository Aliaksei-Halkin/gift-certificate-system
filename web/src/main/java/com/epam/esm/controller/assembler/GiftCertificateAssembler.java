package com.epam.esm.controller.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.GiftCertificateEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler implements SimpleRepresentationModelAssembler<GiftCertificateEntity> {

    @Override
    public void addLinks(EntityModel<GiftCertificateEntity> resource) {
        resource.add(linkTo(methodOn(GiftCertificateController.class)
                .findGiftCertificateById(resource.getContent().getId())).withSelfRel());
        resource.add(linkTo(methodOn(GiftCertificateController.class)
                .findGiftCertificateTags(resource.getContent().getId())).withRel("tags"));
        resource.add(linkTo(methodOn(GiftCertificateController.class)
                .updateGiftCertificate(resource.getContent().getId(), null)).withRel("update"));
        resource.add(linkTo(methodOn(GiftCertificateController.class)
                .updateGiftCertificateField(resource.getContent().getId(), null)).withRel("update_field"));
        resource.add(linkTo(methodOn(GiftCertificateController.class)
                .deleteGiftCertificateById(resource.getContent().getId())).withRel("delete"));
        resource.add(linkTo(methodOn(GiftCertificateController.class)
                .addGiftCertificate(null)).withRel("add_new_certificate"));
        resource.getContent().getTags().forEach(tag -> {
            resource.add(linkTo(methodOn(TagController.class).findTagById(tag.getId())).withSelfRel());
            resource.add(linkTo(methodOn(TagController.class).deleteTagById(tag.getId())).withRel("delete"));
        });
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<GiftCertificateEntity>> resources) {
    }
}
