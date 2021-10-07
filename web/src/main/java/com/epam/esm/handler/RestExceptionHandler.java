package com.epam.esm.handler;

import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.GiftCertificateService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

/**
 * @author Aliaksei Halkin
 */
@RestControllerAdvice
public class RestExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    public static final String INCORRECT_TYPE = "incorrectType";
    public static final String PAGE_NOT_FOUND = "pageNotFound";
    public static final String METHOD_NOT_SUPPORT = "methodNotSupport";
    public static final String BODY_MISSING = "bodyIsMissing";
    public static final String INTERNAL_ERROR = "internalError";
    private final MessageSource messageSource;

    @Autowired
    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorHandler> handleValidationException(ValidationException exception, Locale locale) {
        String message = messageSource.getMessage(exception.getMessageKey(), new Object[]{exception.getMessageValue()},
                locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.BAD_REQUEST);
        LOGGER.log(Level.ERROR, "ValidationException message: {}", message);
        return new ResponseEntity<>(errorHandler, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorHandler> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        Locale locale) {
        String message = messageSource.getMessage(exception.getMessageKey(), new Object[]{exception.getMessageValue()},
                locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.NOT_FOUND);
        LOGGER.log(Level.ERROR, "ResourceNotFoundException message: {}", message);
        return new ResponseEntity<>(errorHandler, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorHandler> handleTypeMismatchException(TypeMismatchException exception, Locale locale) {
        String message = messageSource.getMessage(INCORRECT_TYPE, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.BAD_REQUEST);
        LOGGER.log(Level.ERROR, "TypeMismatchException message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorHandler> handleNoHandlerFoundException(NoHandlerFoundException exception, Locale locale) {
        String message = messageSource.getMessage(PAGE_NOT_FOUND, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.NOT_FOUND);
        LOGGER.log(Level.ERROR, "NoHandlerFoundException message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorHandler> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception, Locale locale) {
        String message = messageSource.getMessage(METHOD_NOT_SUPPORT, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.METHOD_NOT_ALLOWED);
        LOGGER.log(Level.ERROR, "HttpRequestMethodNotSupportedException message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorHandler> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception, Locale locale) {
        String message = messageSource.getMessage(BODY_MISSING, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.BAD_REQUEST);
        LOGGER.log(Level.ERROR, "HttpMessageNotReadableException message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorHandler> handleException(Exception exception, Locale locale) {
        String message = messageSource.getMessage(INTERNAL_ERROR, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.INTERNAL_ERROR);
        LOGGER.log(Level.ERROR, "Exception message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
