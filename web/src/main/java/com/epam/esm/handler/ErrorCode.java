package com.epam.esm.handler;

/**
 * The class represents values for some exceptions.
 *
 * @author Aliaksei Halkin
 */
public final class ErrorCode {
    public static final int NOT_FOUND_ALL = 404;
    public static final int NOT_FOUND_CERTIFICATE = 404_02;
    public static final int NOT_FOUND_TAG = 404_01;
    public static final int BAD_REQUEST_ALL = 400;
    public static final int BAD_REQUEST_CERTIFICATE = 400_02;
    public static final int BAD_REQUEST_TAG = 400_02;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int INTERNAL_ERROR = 500;

    private ErrorCode() {
    }
}
