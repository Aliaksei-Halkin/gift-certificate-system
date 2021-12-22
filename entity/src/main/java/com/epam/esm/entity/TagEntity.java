package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * The entity class represents Tag
 *
 * @author Aliaksei Halkin
 */
@Entity
@Table(name = "tags")
@Where(clause = "active = 1")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagId",
            nullable = false, unique = true)
    private Long id;
    @Column(name = "tagName", unique = true)
    private String name;
    @Column(name = "active")
    private boolean active;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "certificates_has_tags",
            joinColumns = @JoinColumn(name = "tagId"),
            inverseJoinColumns = @JoinColumn(name = "certificateId"))
    private List<GiftCertificateEntity> giftCertificates;

    /**
     * The no-args constructor
     */


    public TagEntity() {
    }

    public TagEntity(Long id, String name, boolean active, List<GiftCertificateEntity> giftCertificates) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.giftCertificates = giftCertificates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<GiftCertificateEntity> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(List<GiftCertificateEntity> giftCertificates) {
        this.giftCertificates = giftCertificates;
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
        TagEntity tag = (TagEntity) o;
        return active == tag.active && Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }


    /**
     * The implementation of the hashCode() method. Uses method hash() of the {@code Objects} class get
     * the hash value of the class members.
     *
     * @return {@code int} value of the hash value fo the all class members
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, active, giftCertificates);
    }


    /**
     * The standard method which represents the Tag in the string value
     *
     * @return {@code String} the Tag
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
