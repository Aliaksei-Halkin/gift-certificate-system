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
    /**
     * The implementation of the equals method. Compare this instance of AbstractEntity to another
     * object.
     *
     * @param o {@code Object} to be compared with this {@code AbstractEntity}
     * @return {@code true} is object to compare is instance of {@code AbstractEntity} and it has the same
     * value of all class members
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return id.equals(that.id);
    }
    /**
     * The implementation of the hashCode() method. Uses method hash() of the {@code Objects} class get
     * the hash value of the class members.
     *
     * @return {@code int} value of the hash value fo the all class members
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * The standard method which represents the AbstractEntity in the string value
     *
     * @return {@code String} the AbstractEntity
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        return sb.toString();
    }
}
