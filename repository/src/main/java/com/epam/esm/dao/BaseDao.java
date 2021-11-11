package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents base dao  for other dao
 *
 * @author Aliaksei Halkin
 */
public interface BaseDao<T, K> {
    Optional<T> findById(K id);

    List<T> findAll();

    long add(T entity);

    void removeById(K id);

    T update(T entity);
}
