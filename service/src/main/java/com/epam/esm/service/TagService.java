package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.Map;
import java.util.Set;

/**
 * The interface represents {@code Tag} service
 *
 * @author Aliaksei Halkin
 */
public interface TagService {

    Set<TagDto> findAllTags(Map<String,String> pages);

    TagDto findTagById(long id);

    TagDto addTag(TagDto tag);

    void deleteTagById(long id);
}
