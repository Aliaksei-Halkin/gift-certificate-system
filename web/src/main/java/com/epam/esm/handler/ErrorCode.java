package com.epam.esm.handler;

/**
 * The class represents values for some exceptions.
 *
 * @author Aliaksei Halkin
 */
public final class ErrorCode {
    public static final int NOT_FOUND = 404404;
    public static final int BAD_REQUEST = 400400;
    public static final int METHOD_NOT_ALLOWED = 405405;
    public static final int INTERNAL_ERROR = 500500;

    private ErrorCode() {
    }
}
