package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
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
     * @param giftCertificateDto Gift certificate to be inserted into storage. Inferred from the request body.
     * @return {@link ResponseEntity} with the inserted gift certificate and its location included.
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDto> addGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto addedGiftCertificateDto = giftCertificateService.addGiftCertificate(giftCertificateDto);
        return new ResponseEntity<>(addedGiftCertificateDto, HttpStatus.OK);
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
     * @param tagDto            Inserted value of the tag.
     * @return {@link ResponseEntity} with the set of tags which belongs to the gift certificate.
     */
    @PutMapping("/{id}/tags")
    public ResponseEntity<Set<TagDto>> addTagToGiftCertificate(@PathVariable("id") long giftCertificateId,
                                                               @RequestBody TagDto tagDto) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.addTagToGiftCertificate(giftCertificateId, tagDto);
        return new ResponseEntity<>(giftCertificateDto.getTags(), HttpStatus.OK);
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
    public ResponseEntity<GiftCertificateDto> findGiftCertificateById(@PathVariable("id") long id) {
        GiftCertificateDto giftCertificate = giftCertificateService.findGiftCertificateById(id);
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
    public ResponseEntity<Set<TagDto>> findGiftCertificateTags(@PathVariable("id") long certificateId) {
        GiftCertificateDto giftCertificate = giftCertificateService.findGiftCertificateById(certificateId);
        Set<TagDto> tags = giftCertificate.getTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    /**
     * Find the gift certificate in the storage by various parameter passed as a parameter in the request URI.
     * If there is no parameters method returns all the gift certificates in the storage.
     * <p>
     * Annotated by {@link GetMapping} with no parameters. Therefore, processes GET requests at /certificates.
     * <p>
     * Accepts optional request parameters {@code tagName}, {@code certificateName}, {@code certificateDescription},
     * {@code order}, {@code direction}. All parameters can be used in conjunction.
     * <p>
     * The {@code order} might contain one the following values:
     * {@code name} or {@code description}. If {@code direction} not defined will be selected {@code direction} by {@code asc}.
     * <p>
     * The {@code direction} might contain one the following values: {@code desc} or {@code asc}.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param tagName                The parameter used to find gift certificates by tag name.
     * @param certificateName        The parameter used to find gift certificates by certificate name.
     * @param certificateDescription The parameter used to find gift certificates by certificate description.
     * @param order                  The parameter used to choose order of sorting certificates.
     * @param direction              The parameter used to choose direction of sorting certificates.
     * @return {@link ResponseEntity} with the list of the gift certificates.
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificatesByParameters
    (@RequestParam(value = "tagName", required = false) String tagName,
     @RequestParam(value = "certificateName", required = false) String certificateName,
     @RequestParam(value = "certificateDescription", required = false) String certificateDescription,
     @RequestParam(value = "order", required = false) String order,
     @RequestParam(value = "direction", required = false) String direction) {
        QueryParameter queryParameter = new QueryParameter(tagName, certificateName, certificateDescription, order, direction);
        List<GiftCertificateDto> giftCertificates = giftCertificateService.findGiftCertificatesByParameters(queryParameter);
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
     * @param giftCertificateDto Updated value of the gift certificate.
     * @param giftCertificateId  The identifier of the gift certificate to be updated. Inferred from the request URI.
     * @return {@link ResponseEntity} with the updated gift certificate.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@PathVariable("id") long giftCertificateId,
                                                                    @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto updatedGiftCertificateDto = giftCertificateService
                .updateGiftCertificate(giftCertificateId, giftCertificateDto);
        return new ResponseEntity<>(updatedGiftCertificateDto, HttpStatus.OK);
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

}
