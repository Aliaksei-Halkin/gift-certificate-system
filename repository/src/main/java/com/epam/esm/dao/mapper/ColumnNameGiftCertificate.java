package com.epam.esm.dao.mapper;

/**
 * The {@code ColumnName} class has fields equals fields in database
 *
 * @author Aliaksei Halkin
 */
public enum ColumnNameGiftCertificate {
    CERTIFICATE_ID("certificateId"),
    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    DURATION("duration"),
    CREATE_DATE("create_date"),
    LAST_UPDATE_DATE("last_update_date"),
    ACTIVE("active");

    private final String name;

    private ColumnNameGiftCertificate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
