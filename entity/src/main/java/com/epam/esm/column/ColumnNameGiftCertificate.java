package com.epam.esm.column;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code ColumnName} class has fields equals fields in database
 *
 * @author Aliaksei Halkin
 */

public class ColumnNameGiftCertificate {
    public static final String CERTIFICATE_TABLE = "gift_certificates";
    public static final String CERTIFICATE_ID = "certificateId";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String DURATION = "duration";
    public static final String CREATE_DATE = "create_date";
    public static final String LAST_UPDATE_DATE = "last_update_date";
    public static final String ACTIVE = "active";

    private ColumnNameGiftCertificate() {
    }
}
