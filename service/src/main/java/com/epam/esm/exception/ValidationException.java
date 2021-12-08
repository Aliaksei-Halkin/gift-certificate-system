package com.epam.esm.exception;

/**
 * The class represents custom exceptions for validation
 *
 * @author Aliaksei Halkin
 */
public class ValidationException extends RuntimeException {
    private String messageKey;
    private Object messageValue;
    private IdentifierEntity identification;

    public ValidationException(String messageKey, Object messageValue, IdentifierEntity id) {
        this.messageKey = messageKey;
        this.messageValue = messageValue;
        this.identification =id;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object getMessageValue() {
        return messageValue;
    }

    public IdentifierEntity getIdentification() {
        return identification;
    }
}
