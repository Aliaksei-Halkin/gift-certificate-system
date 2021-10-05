package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * @author Aliaksei Halkin
 */
public interface TagDao extends BaseDao<Tag, Long> {
    Optional<Tag> findTagByName(String name);
}
