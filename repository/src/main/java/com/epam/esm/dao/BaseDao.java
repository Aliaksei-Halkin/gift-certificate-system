package com.epam.esm.dao;

import com.epam.esm.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author Aliaksei Halkin
 */
public interface BaseDao<T extends AbstractEntity<K>, K> {
    Optional<T> findById(K id);

    List<T> findAll();

    long add(T entity);

    void removeById(K id);

    T update(T entity);
}
