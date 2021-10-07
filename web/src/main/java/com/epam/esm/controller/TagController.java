package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author Aliaksei Halkin
 */
@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagDto> addTag(@RequestBody TagDto tagDto) {
        TagDto addedTagDto = tagService.addTag(tagDto);
        return new ResponseEntity<>(addedTagDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findTagById(@PathVariable("id") long id) {
        TagDto tagDto = tagService.findTagById(id);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<TagDto>> findAllTags() {
        Set<TagDto> tagsDto = tagService.findAllTags();
        return new ResponseEntity<>(tagsDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable("id") long id) {
        tagService.deleteTagById(id);
    }
}
