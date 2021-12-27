package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class TagDto extends RepresentationModel<TagDto> {

    private Long id;
    private String name;
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(id, tagDto.id) && Objects.equals(name, tagDto.name) && Objects.equals(active, tagDto.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, active);
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
