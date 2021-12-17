package com.epam.esm.controller;

import com.epam.esm.controller.assembler.GiftCertificateAssembler;
import com.epam.esm.controller.assembler.TagAssembler;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateField;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The {@code GiftCertificateController} class is an endpoint of the API
 * which allows its users to perform CRUD operations on gift certificates.
 * <p>
 * {@code GiftCertificateController} is accessed by sending request to /certificates
 * and the response produced by {@code GiftCertificateController} carries application/json
 * type of content(except for {@link #deleteGiftCertificateById} method, which send no content back to the user).
 * <p>
 * {@code GiftCertificateController} provides the user with methods to add gift certificate({@link #addGiftCertificate}),
 * add tag to gift certificate({@link #addTagToGiftCertificate}), find gift certificate by id({@link #findGiftCertificateById}),
 * find gift certificate tags({@link #findGiftCertificateTags}), update ({@link #updateGiftCertificate})
 * and delete by id({@link #deleteGiftCertificateById}) gift certificates from storage.
 *
 * @author Aliaksei Halkin
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateAssembler giftCertificateAssembler;
    private final TagAssembler tagAssembler;

    /**
     * Injects an object of a class implementing {@link GiftCertificateService}.
     *
     * @param giftCertificateService An object of a class implementing {@link GiftCertificateService}.
     */
    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateAssembler giftCertificateAssembler,
                                     TagAssembler tagAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateAssembler = giftCertificateAssembler;
        this.tagAssembler = tagAssembler;
    }

    /**
     * Inserts the gift certificate passed in the request body into the storage.
     * <p>
     * Annotated with {@link PostMapping} with parameter consumes = "application/json",
     * which implies that the method processes POST requests at /certificates and that the
     * information about the new gift certificate must be carried in request body in JSON format.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param giftCertificate Gift certificate to be inserted into storage. Inferred from the request body.
     * @return {@link ResponseEntity} with the inserted gift certificate and its location included.
     */
    @PostMapping
    public ResponseEntity<EntityModel<GiftCertificateDto>> addGiftCertificate(@RequestBody GiftCertificateDto giftCertificate) {
        GiftCertificateDto addedGiftCertificate = giftCertificateService.addGiftCertificate(giftCertificate);
        return new ResponseEntity<>(giftCertificateAssembler.toModel(addedGiftCertificate), HttpStatus.CREATED);
    }

    /**
     * Inserts the tag passed in the request body into the storage.
     * <p>
     * Annotated with {@link PutMapping} with parameter consumes = "application/json",
     * which implies that the method processes PUT requests at /certificates/id/tags where id is the identifier of the
     * gift certificate where tag to be inserted. Information about the new tag must be carried in request body
     * in JSON format.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param giftCertificateId The identifier of the gift certificate to be updated. Inferred from the request URI.
     * @param tag               Inserted value of the tag.
     * @return {@link ResponseEntity} with the set of tags which belongs to the gift certificate.
     */
    @PutMapping("/{id}/tags")
    public ResponseEntity<CollectionModel<EntityModel<TagDto>>> addTagToGiftCertificate(@PathVariable("id") long giftCertificateId,
                                                                                        @RequestBody TagDto tag) {
        GiftCertificateDto giftCertificate = giftCertificateService.addTagToGiftCertificate(giftCertificateId, tag);
        return new ResponseEntity<>(tagAssembler.toCollectionModel(giftCertificate.getTags()), HttpStatus.OK);
    }

    /**
     * Returns the gift certificate with the specified identifier from the storage.
     * <p>
     * Annotated by {@link GetMapping} with parameter value = "/{id}". Therefore, processes GET requests at
     * /certificates/id, where id is the identifier of the requested gift certificate
     * represented by a natural number.
     * <p>
     * If there is no gift certificate with the specified id response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @param id Identifier of the requested gift certificate. Inferred from the request URI.
     * @return {@link ResponseEntity} with found gift certificate.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> findGiftCertificateById(@PathVariable("id") long id) {
        GiftCertificateDto giftCertificate = giftCertificateService.findGiftCertificateById(id);
        return new ResponseEntity<>(giftCertificateAssembler.toModel(giftCertificate), HttpStatus.OK);
    }

    /**
     * Returns the set of tags which belongs to gift certificate with the specified identifier from the storage.
     * <p>
     * Annotated by {@link GetMapping} with parameter value = "/{id}/tags". Therefore, processes GET requests at
     * /certificates/id/tags, where id is the identifier of the requested gift certificate
     * represented by a natural number.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param certificateId Identifier of the requested gift certificate. Inferred from the request URI.
     * @return {@link ResponseEntity} with the set of tags which belongs to the gift certificate.
     */
    @GetMapping("/{id}/tags")
    public ResponseEntity<CollectionModel<EntityModel<TagDto>>> findGiftCertificateTags(@PathVariable("id") long certificateId) {
        GiftCertificateDto giftCertificate = giftCertificateService.findGiftCertificateById(certificateId);
        Set<TagDto> tags = giftCertificate.getTags();
        return new ResponseEntity<>(tagAssembler.toCollectionModel(tags), HttpStatus.OK);
    }


    /**
     * Find gift certificates in the storage by various parameter passed as a parameter in the request URI.
     * If there is no parameters method returns all gift certificates in the storage.
     * <p>
     * Annotated by {@link GetMapping} with no parameters. Therefore, processes GET requests at /certificates.
     * <p>
     * Accepts optional request parameters {@code tagNames}, {@code name}, {@code description},
     * {@code order}, {@code page}, {@code per_page}. All parameters can be used in conjunction.
     * <p>
     * The {@code order} might contain one the following values:
     * {@code name} or {@code -name} and {@code description} or {@code -description}.
     * Minus sign indicates descending order. Default order is ascending without any signs.
     * <p>
     * The {@code page} contains number of the page. The {@code per_page} show how many elements will be displayed on the page.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param queryParameters The parameters used to find gift certificates.
     * @return {@link ResponseEntity} with the list of the gift certificates.
     */
    @GetMapping("/selection")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificateDto>>> findGiftCertificatesByParameters
    (@RequestBody Map<String, String> queryParameters) {
        List<GiftCertificateDto> giftCertificates = giftCertificateService.findGiftCertificatesByParameters(queryParameters);
        return new ResponseEntity<>(giftCertificateAssembler.toCollectionModel(giftCertificates), HttpStatus.OK);
    }

    /**
     * Updates the gift certificate in the storage using {@code giftCertificateDto} passed as a parameter.
     * <p>
     * Annotated with{@link PutMapping} with parameter consumes = "application/json",
     * which implies that the method processes PUT requests at /certificates/id, where id is the identifier of the
     * certificate to be updated represented by a natural number and that the
     * information about the updated gift certificate must be carried in request body in JSON format.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param giftCertificate   Updated value of the gift certificate.
     * @param giftCertificateId The identifier of the gift certificate to be updated. Inferred from the request URI.
     * @return {@link ResponseEntity} with the updated gift certificate.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateGiftCertificate(@PathVariable("id") long giftCertificateId,
                                                                                    @RequestBody GiftCertificateDto giftCertificate) {
        GiftCertificateDto updatedGiftCertificate = giftCertificateService
                .updateGiftCertificate(giftCertificateId, giftCertificate);
        return new ResponseEntity<>(giftCertificateAssembler.toModel(updatedGiftCertificate), HttpStatus.OK);
    }

    /**
     * Deletes the gift certificate with the specified id from the storage.
     * <p>
     * Annotated with{@link DeleteMapping} with parameter value = "/{id}",
     * which implies that the method processes DELETE requests at
     * /certificates/id, where id is the identifier of the gift certificate to be deleted
     * represented by a natural number.
     * <p>
     * The default response status is 200 - Ok.
     *
     * @param id The identifier of the gift certificate to be deleted. Inferred from the request URI.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> deleteGiftCertificateById(@PathVariable("id") long id) {
        giftCertificateService.deleteGiftCertificateById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Find all gift certificates in the storage.
     *
     * @return the list of certificates
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificateDto>>> findAllGiftCertificates(@RequestBody Map<String, String> queryParameters) {
        List<GiftCertificateDto> giftCertificates = giftCertificateService.findAllCertificates(queryParameters);
        return new ResponseEntity<>(giftCertificateAssembler.toCollectionModel(giftCertificates), HttpStatus.OK);
    }

    /**
     * The method represents updating one value from next fields: name, description, price or duration.
     *
     * @param id                   GiftCertificate id
     * @param giftCertificateField field with value for changing
     * @return updated GiftCertificate
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateGiftCertificateField(@PathVariable Long id,
                                                                                         @RequestBody GiftCertificateField giftCertificateField) {
        GiftCertificateDto updatedGiftCertificate = giftCertificateService.updateGiftCertificateField(id, giftCertificateField);
        return new ResponseEntity<>(giftCertificateAssembler.toModel(updatedGiftCertificate), HttpStatus.OK);
    }

}
