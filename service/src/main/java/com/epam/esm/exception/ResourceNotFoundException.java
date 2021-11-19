package com.epam.esm.exception;

/**
 * The class represents custom exceptions for service classes
 *
 * @author Aliaksei Halkin
 */
public class ResourceNotFoundException extends RuntimeException {
    private String messageKey;
    private Object messageValue;
    private IdentifierEntity identification;

    public ResourceNotFoundException(String messageKey, Object messageValue, IdentifierEntity identification) {
        this.messageKey = messageKey;
        this.messageValue = messageValue;
        this.identification = identification;
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
