package com.epam.esm.dto;

public class GiftCertificateField {

    private String fieldName;
    private String fieldValue;

    public enum FieldName {
        NAME,
        DESCRIPTION,
        PRICE,
        DURATION
    }

    public GiftCertificateField() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

}