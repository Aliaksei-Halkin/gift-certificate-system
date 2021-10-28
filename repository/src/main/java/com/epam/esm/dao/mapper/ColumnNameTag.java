package com.epam.esm.dao.mapper;

/**
 * @author Aliaksei Halkin
 */
public enum ColumnNameTag {
    TAG_ID("tagId"),
    TAG_NAME("tagName"),
    ACTIVE("active");

    private final String name;

    private ColumnNameTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
