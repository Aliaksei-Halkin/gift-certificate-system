package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * The entity class represents  GiftCertificate
 *
 * @author Aliaksei Halkin
 */
public class GiftCertificate extends AbstractEntity<Long> {
    /**
     * The name of GiftCertificate
     */
    private String name;
    /**
     * The description of GiftCertificate
     */
    private String description;
    /**
     * The price of GiftCertificate
     */
    private BigDecimal price;
    /**
     * The duration of GiftCertificate
     */
    private int duration;
    /**
     * The date and time of GiftCertificate creation.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;
    /**
     * The date and time of GiftCertificate creation.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateDate;
    /**
     * The set of tags in this Gift certificate
     */
    private Set<Tag> tags;

    /**
     * The no-args constructor
     */
    public GiftCertificate() {
    }

    /**
     * The constructor with all parameters, used to create instance of {@code GiftCertificate} which already has
     * unique {@param id} and to retrieve information about certificate from data storage.
     *
     * @param id          {@code Long} value of unique certificate identification number
     * @param name        {@code String} represents the name of gift certificate
     * @param description {@code String} represents the description of gift certificate
     * @param price       {@code BigDecimal} represents the cost of gift certificate
     * @param duration    {@code int} represents the duration of gift certificate
     * @param createdDate {@code LocalDate} of gift certificate creation
     * @param updateDate  {@code LocalDate} of gift certificate creation
     * @param tags        {@code Set} represents set of tags
     */
    public GiftCertificate(Long id, String name, String description, BigDecimal price,
                           int duration, LocalDateTime createdDate, LocalDateTime updateDate, Set<Tag> tags) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * The implementation of the equals method. Compare this instance of GiftCertificate to another
     * object.
     *
     * @param o {@code Object} to be compared with this {@code GiftCertificate}
     * @return {@code true} is object to compare is instance of {@code GiftCertificate} and it has the same
     * value of all class members
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return duration == that.duration && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(price, that.price)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(updateDate, that.updateDate)
                && Objects.equals(tags, that.tags);
    }

    /**
     * The implementation of the hashCode() method. Uses method hash() of the {@code Objects} class get
     * the hash value of the class members.
     *
     * @return {@code int} value of the hash value fo the all class members
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, price, duration, createdDate,
                updateDate, tags);
    }

    /**
     * The standard method which represents the GiftCertificate in the string value
     *
     * @return {@code String} the GiftCertificate
     */
    @Override
    public String toString() {
        return "GiftCertificate{" + super.toString() +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createdDate +
                ", updateDate=" + updateDate +
                ", tags=" + tags +
                '}';
    }
}
