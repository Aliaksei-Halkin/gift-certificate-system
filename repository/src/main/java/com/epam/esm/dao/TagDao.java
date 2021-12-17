package com.epam.esm.dao;

import com.epam.esm.entity.TagEntity;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents {@code Tag} dao
 *
 * @author Aliaksei Halkin
 */
public interface TagDao extends BaseDao<TagEntity, Long> {
    void removeById(Long id);

    Optional<TagEntity> findByName(String name);

    void changeActiveForTag(String name);

    List<TagEntity> findAll(int page, int perPage);

    long countTotalRows(int page, int perPage);
}
