package com.epam.esm.exception;

/**
 * The class represents custom exceptions for validation
 *
 * @author Aliaksei Halkin
 */
public class ValidationException extends RuntimeException {
    private String messageKey;
    private Object messageValue;

    public ValidationException(String messageKey, Object messageValue) {
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
