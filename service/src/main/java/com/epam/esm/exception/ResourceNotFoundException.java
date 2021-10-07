package com.epam.esm.exception;

/**
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
