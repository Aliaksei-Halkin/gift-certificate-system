package com.epam.esm.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * The abstract class represents basic entity.
 *
 * @author Aliaksei Halkin
 * @version 1.0
 */
public abstract class AbstractEntity<K> implements Serializable {
    private K id;

    /**
     * Instantiates a new Entity.
     */
    public AbstractEntity() {
    }

    /**
     * Instantiates a new Entity.
     *
     * @param id the id
     */
    public AbstractEntity(K id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public K getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(K id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        return sb.toString();
    }
}
