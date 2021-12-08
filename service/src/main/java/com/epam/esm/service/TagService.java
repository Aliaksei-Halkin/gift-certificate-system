package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.Map;
import java.util.Set;

/**
 * The interface represents {@code Tag} service
 *
 * @author Aliaksei Halkin
 */
public interface TagService {

    Set<Tag> findAllTags(Map<String,String> pages);

    Tag findTagById(long id);

    Tag addTag(Tag tag);

    void deleteTagById(long id);
}
