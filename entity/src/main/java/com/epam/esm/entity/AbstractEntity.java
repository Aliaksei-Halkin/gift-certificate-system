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
    private boolean active;

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
    public AbstractEntity(K id, boolean active) {
        this.id = id;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        return active == that.active && Objects.equals(id, that.id);
    }

    /**
     * The implementation of the hashCode() method. Uses method hash() of the {@code Objects} class get
     * the hash value of the class members.
     *
     * @return {@code int} value of the hash value fo the all class members
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, active);
    }

    /**
     * The standard method which represents the AbstractEntity in the string value
     *
     * @return {@code String} the AbstractEntity
     */
    @Override
    public String toString() {
        return
                "id=" + id + ", active=" + active + ", ";
    }
}
