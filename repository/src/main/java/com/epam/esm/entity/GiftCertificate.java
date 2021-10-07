package com.epam.esm.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Aliaksei Halkin
 */
public class GiftCertificate extends AbstractEntity<Long> {
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Set<Tag> tags;

    public GiftCertificate() {
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, price, duration, createdDate,
                updateDate, tags);
    }

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
