package com.epam.esm.exception;

/**
 * The class represents custom exceptions for service classes
 *
 * @author Aliaksei Halkin
 */
public class ResourceNotFoundException extends RuntimeException {
    private String messageKey;
    private Object messageValue;

    public ResourceNotFoundException(String messageKey, Object messageValue) {
        this.messageKey = messageKey;
        this.messageValue = messageValue;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object getMessageValue() {
        return messageValue;
    }
}
