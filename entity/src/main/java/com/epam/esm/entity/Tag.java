package com.epam.esm.entity;

import java.util.Objects;

/**
 * The entity class represents Tag
 *
 * @author Aliaksei Halkin
 */
public class Tag extends AbstractEntity<Long> {
    private String name;

    /**
     * The no-args constructor
     */
    public Tag() {
    }

    /**
     * The constructor with all parameters, used to create instance of {@code Tag} which already has
     * unique {@param id} and to retrieve information about tag from data storage.
     *
     * @param id   {@code Long} value of unique Tag identification number
     * @param name {@code String} represents name of tag
     */
    public Tag(Long id, String name, boolean active) {
        super(id,active);
        this.name = name;
    }

    /**
     * Standard getter method to access private class member.
     *
     * @return {@code String}  name represents name of tag
     */
    public String getName() {
        return name;
    }

    /**
     * The standard setter method to access private class member.
     *
     * @param name {@code String} represents name of tag
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The implementation of the equals method. Compare this instance of Tag to another
     * object.
     *
     * @param o {@code Object} to be compared with this {@code Tag}
     * @return {@code true} is object to compare is instance of {@code Tag} and it has the same
     * value of all class members
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    /**
     * The implementation of the hashCode() method. Uses method hash() of the {@code Objects} class get
     * the hash value of the class members.
     *
     * @return {@code int} value of the hash value fo the all class members
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    /**
     * The standard method which represents the Tag in the string value
     *
     * @return {@code String} the Tag
     */
    @Override
    public String toString() {
        return "Tag{" + super.toString() +
                "name='" + name + '\'' +
                '}';
    }
}
