package com.epam.esm.controller;

import com.epam.esm.controller.assembler.TagAssembler;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * The {@code TagController} class is an endpoint of the API
 * which allows its users to perform CRD operations on tag.
 * <p>
 * {@code TagController} is accessed by sending request to /tags
 * and the response produced by {@code TagController} carries application/json
 * type of content(except for {@link #deleteTagById} method, which send no content back to the user).
 * <p>
 * {@code TagController} provides the user with methods to add tag({@link #addTag}),
 * find tag by id({@link #findTagById}), find all tags({@link #findAllTags})
 * and delete by id({@link #deleteTagById}) tag from storage.
 *
 * @author Aliaksei Halkin
 */
@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final TagAssembler tagAssembler;

    /**
     * Injects an object of a class implementing {@link TagService}.
     *
     * @param tagService An object of a class implementing {@link TagService}.
     */
    @Autowired
    public TagController(TagService tagService, TagAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }

    /**
     * Inserts the tag passed in the request body into the storage.
     * <p>
     * Annotated with {@link PostMapping} with parameter consumes = "application/json",
     * which implies that the method processes POST requests at /tags and that the
     * information about the new tag must be carried in request body in JSON format.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param tag Tag to be inserted into storage. Inferred from the request body.
     * @return {@link ResponseEntity} with the inserted tag and its location included.
     */
    @PostMapping
    public ResponseEntity<EntityModel<TagDto>> addTag(@RequestBody TagDto tag) {
        TagDto addedTag = tagService.addTag(tag);
        return new ResponseEntity<>(tagAssembler.toModel(addedTag), HttpStatus.CREATED);
    }

    /**
     * Returns the tag with the specified identifier from the storage.
     * <p>
     * Annotated by {@link GetMapping} with parameter value = "/{id}". Therefore, processes GET requests at
     * /tags/id, where id is the identifier of the requested tag represented by a natural number.
     * <p>
     * If there is no tag with the specified id response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @param id Identifier of the requested tag. Inferred from the request URI.
     * @return {@link ResponseEntity} with found tag.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TagDto>> findTagById(@PathVariable("id") long id) {
        TagDto tag = tagService.findTagById(id);
        return new ResponseEntity<>(tagAssembler.toModel(tag), HttpStatus.OK);
    }

    /**
     * Returns all the gift certificates in the storage.
     * <p>
     * Annotated by {@link GetMapping} with no parameters. Therefore, processes GET requests at /tags.
     * <p>
     * The default response status is 200 - OK.
     *
     * @return {@link ResponseEntity} with the list of the gift certificates.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TagDto>>> findAllTags(@RequestBody Map<String, String> pages) {
        Set<TagDto> tags = tagService.findAllTags(pages);
        return new ResponseEntity<>(tagAssembler.toCollectionModel(tags), HttpStatus.OK);
    }

    /**
     * Deletes the tag with the specified id from the storage.
     * <p>
     * Annotated with{@link DeleteMapping} with parameter value = "/{id}",
     * which implies that the method processes DELETE requests at
     * /tags/id, where id is the identifier of the tags to be deleted
     * represented by a natural number.
     * <p>
     * The default response status is 200 - Ok.
     *
     * @param id The identifier of the tag to be deleted. Inferred from the request URI.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> deleteTagById(@PathVariable("id") long id) {
        tagService.deleteTagById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
