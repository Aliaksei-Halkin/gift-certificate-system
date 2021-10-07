package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.Set;

/**
 * @author Aliaksei Halkin
 */
public interface TagService {

    Set<TagDto> findAllTags();

    TagDto findTagById(long id);

    TagDto addTag(TagDto tagDto);

    void deleteTagById(long id);
}
