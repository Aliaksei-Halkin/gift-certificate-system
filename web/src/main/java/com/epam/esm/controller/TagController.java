package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Injects an object of a class implementing {@link TagService}.
     *
     * @param tagService An object of a class implementing {@link TagService}.
     */
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
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
     * @param tagDto Tag to be inserted into storage. Inferred from the request body.
     * @return {@link ResponseEntity} with the inserted tag and its location included.
     */
    @PostMapping
    public ResponseEntity<TagDto> addTag(@RequestBody TagDto tagDto) {
        TagDto addedTagDto = tagService.addTag(tagDto);
        return new ResponseEntity<>(addedTagDto, HttpStatus.OK);
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
    public ResponseEntity<TagDto> findTagById(@PathVariable("id") long id) {
        TagDto tagDto = tagService.findTagById(id);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
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
    public ResponseEntity<Set<TagDto>> findAllTags() {
        Set<TagDto> tagsDto = tagService.findAllTags();
        return new ResponseEntity<>(tagsDto, HttpStatus.OK);
    }

    /**
     * Deletes the tag with the specified id from the storage.
     * <p>
     * Annotated with{@link DeleteMapping} with parameter value = "/{id}",
     * which implies that the method processes DELETE requests at
     * /tags/id, where id is the identifier of the tags to be deleted
     * represented by a natural number.
     * <p>
     * The default response status is 204 - No Content, as the response body is empty.
     *
     * @param id The identifier of the tag to be deleted. Inferred from the request URI.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable("id") long id) {
        tagService.deleteTagById(id);
    }
}
