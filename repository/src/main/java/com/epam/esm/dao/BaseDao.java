package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * The interface represents base dao  for other dao
 *
 * @author Aliaksei Halkin
 */
public interface BaseDao<T, K> {
    Optional<T> findById(K id);

    long add(T entity);

    T update(T entity);

}
