package com.epam.esm.entity;

import com.epam.esm.column.ColumnNameGiftCertificatesHasTags;
import com.epam.esm.column.ColumnNameTag;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * The entity class represents Tag
 *
 * @author Aliaksei Halkin
 */
@Entity
@Table(name = ColumnNameTag.TAG_TABLE)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ColumnNameTag.TAG_ID,
            nullable = false, unique = true)
    private Long id;
    @Column(name = ColumnNameTag.TAG_NAME, unique = true)
    private String name;
    @Column(name = ColumnNameTag.ACTIVE)
    private boolean active;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = ColumnNameGiftCertificatesHasTags.TABLE,
            joinColumns = @JoinColumn(name = ColumnNameTag.TAG_ID),
            inverseJoinColumns = @JoinColumn(name = ColumnNameGiftCertificatesHasTags.CERTIFICATE_ID))
    private List<GiftCertificate> giftCertificates;

    /**
     * The no-args constructor
     */


    public Tag() {
    }

    public Tag(Long id, String name, boolean active, List<GiftCertificate> giftCertificates) {
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

    public List<GiftCertificate> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(List<GiftCertificate> giftCertificates) {
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
        Tag tag = (Tag) o;
        return active == tag.active && Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(giftCertificates, tag.giftCertificates);
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
                ", giftCertificates=" + giftCertificates +
                '}';
    }
}
