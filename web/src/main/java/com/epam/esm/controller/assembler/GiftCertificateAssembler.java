package com.epam.esm.controller.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler extends RepresentationModelAssemblerSupport<GiftCertificate, GiftCertificateDto>{

    @Autowired
    public TagAssembler tagAssembler;

    @Override
    public GiftCertificateDto toModel(GiftCertificate certificate){
        certificate.getTags()
                .stream()
                .map(tagAssembler::toModel)
                .collect(toList());
    }
}