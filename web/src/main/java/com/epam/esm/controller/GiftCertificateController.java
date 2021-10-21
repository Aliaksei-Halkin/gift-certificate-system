package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.QueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    /**
     * Injects an object of a class implementing {@link GiftCertificateService}.
     *
     * @param giftCertificateService An object of a class implementing {@link GiftCertificateService}.
     */
    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
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
    public ResponseEntity<GiftCertificate> addGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate addedGiftCertificate = giftCertificateService.addGiftCertificate(giftCertificate);
        return new ResponseEntity<>(addedGiftCertificate, HttpStatus.OK);
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
    public ResponseEntity<Set<Tag>> addTagToGiftCertificate(@PathVariable("id") long giftCertificateId,
                                                            @RequestBody Tag tag) {
        GiftCertificate giftCertificate = giftCertificateService.addTagToGiftCertificate(giftCertificateId, tag);
        return new ResponseEntity<>(giftCertificate.getTags(), HttpStatus.OK);
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
    public ResponseEntity<GiftCertificate> findGiftCertificateById(@PathVariable("id") long id) {
        GiftCertificate giftCertificate = giftCertificateService.findGiftCertificateById(id);
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
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
    public ResponseEntity<Set<Tag>> findGiftCertificateTags(@PathVariable("id") long certificateId) {
        GiftCertificate giftCertificate = giftCertificateService.findGiftCertificateById(certificateId);
        Set<Tag> tags = giftCertificate.getTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }


    /**
     * Find the gift certificate in the storage by various parameter passed as a parameter from body QueryParameter
     * If there is no parameters method returns all the gift certificates in the storage.
     * <p>
     * Accepts optional request parameters in RequestBody:  {@code tagName}, {@code certificateName}, {@code certificateDescription},
     * {@code order}, {@code direction}. All parameters can be used in conjunction.
     * <p>
     * The {@code order} might contain one the following values:
     * {@code name} or {@code description}. If {@code direction} not defined will be selected {@code direction} by {@code asc}.
     * <p>
     * The {@code direction} might contain one the following values: {@code desc} or {@code asc}.
     * <p>
     * The default response status is 200 - OK.
     * <p>
     *
     * @param queryParameter The parameters when include {@link QueryParameter} use to find gift certificates by certificate.
     * @return {@link ResponseEntity} with the list of the gift certificates.
     */
    @GetMapping("/selection")
    public ResponseEntity<List<GiftCertificate>> findGiftCertificatesByParameters
    (@RequestBody QueryParameter queryParameter) {
        List<GiftCertificate> giftCertificates = giftCertificateService.findGiftCertificatesByParameters(queryParameter);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
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
    public ResponseEntity<GiftCertificate> updateGiftCertificate(@PathVariable("id") long giftCertificateId,
                                                                 @RequestBody GiftCertificate giftCertificate) {
        GiftCertificate updatedGiftCertificate = giftCertificateService
                .updateGiftCertificate(giftCertificateId, giftCertificate);
        return new ResponseEntity<>(updatedGiftCertificate, HttpStatus.OK);
    }

    /**
     * Deletes the gift certificate with the specified id from the storage.
     * <p>
     * Annotated with{@link DeleteMapping} with parameter value = "/{id}",
     * which implies that the method processes DELETE requests at
     * /certificates/id, where id is the identifier of the gift certificate to be deleted
     * represented by a natural number.
     * <p>
     * The default response status is 204 - No Content, as the response body is empty.
     *
     * @param id The identifier of the gift certificate to be deleted. Inferred from the request URI.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGiftCertificateById(@PathVariable("id") long id) {
        giftCertificateService.deleteGiftCertificateById(id);
    }

    /**
     * Find all gift certificates in the storage.
     *
     * @return the list of certificates
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificate>> findAllGiftCertificates() {
        List<GiftCertificate> giftCertificates = giftCertificateService.findGiftCertificatesByParameters(new QueryParameter());
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

}
