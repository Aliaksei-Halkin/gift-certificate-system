package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents {@code Tag} dao
 *
 * @author Aliaksei Halkin
 */
public interface TagDao extends BaseDao<Tag, Long> {
    void removeById(Long id);

    Optional<Tag> findByName(String name);

    void changeActiveForTag(String name);

    List<Tag> findAll();
}
