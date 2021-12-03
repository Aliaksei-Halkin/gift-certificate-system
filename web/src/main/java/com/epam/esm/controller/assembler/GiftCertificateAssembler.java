package com.epam.esm.controller.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler extends RepresentationModelAssemblerSupport<GiftCertificate, GiftCertificate> {


    public GiftCertificateAssembler() {
        super(GiftCertificateController.class, GiftCertificate.class);
    }


    @Override
    public CollectionModel toCollectionModel(Iterable<? extends GiftCertificate> entities) {
        CollectionModel<GiftCertificate> certificateModels = super.toCollectionModel(entities);
        certificateModels.add(linkTo(methodOn(GiftCertificateController.class).findAllGiftCertificates(new HashMap<>())).withSelfRel());
        return certificateModels;
    }

    @Override
    public GiftCertificate toModel(GiftCertificate entity) {
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .findGiftCertificateById(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .findGiftCertificateTags(entity.getId())).withRel("tags"));
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .updateGiftCertificate(entity.getId(), null)).withRel("update"));
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .updateGiftCertificateField(entity.getId(), null)).withRel("update_field"));
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .deleteGiftCertificateById(entity.getId())).withRel("delete"));
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .addGiftCertificate(null)).withRel("add_new_certificate"));
        toCertificateModel(entity.getTags());
        return entity;
    }

    private void toCertificateModel(Set<Tag> tags) {
        if (tags.isEmpty()) {
            return;
        }
        for (Tag entity : tags) {
            entity.add(linkTo(methodOn(TagController.class).findTagById(entity.getId())).withSelfRel());
            entity.add(linkTo(methodOn(TagController.class).deleteTagById(entity.getId())).withRel("delete"));
        }
    }
}
