package com.epam.esm.controller.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler implements SimpleRepresentationModelAssembler<GiftCertificate> {

    @Override
    public void addLinks(EntityModel<GiftCertificate> resource) {
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
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<GiftCertificate>> resources) {
    }
}
