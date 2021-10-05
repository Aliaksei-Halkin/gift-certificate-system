package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * @author Aliaksei Halkin
 */
public class TagDaoImpl implements TagDao {
    @Override
    public Optional findById(long id) {
        return Optional.empty();
    }

    @Override
    public long add(Object entity) {
        return 0;
    }

    @Override
    public void removeById(long id) {

    }

    @Override
    public Object update(Object entity) {
        return null;
    }

    @Override
    public List<Tag> findAll() {
        return null;
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return Optional.empty();
    }
}
