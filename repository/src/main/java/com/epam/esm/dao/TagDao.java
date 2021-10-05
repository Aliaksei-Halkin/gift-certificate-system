package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * @author Aliaksei Halkin
 */
public interface TagDao extends BaseDao {
    List<Tag> findAll();

    Optional<Tag> findTagByName(String name);

}
