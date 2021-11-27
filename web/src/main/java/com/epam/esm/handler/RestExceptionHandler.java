package com.epam.esm.handler;

import com.epam.esm.exception.IdentifierEntity;
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
 * The RestExceptionHandler class is handling exceptions in specific  handler methods.
 *
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
    /**
     * Strategy interface for resolving messages, with support for the parameterization and
     * internationalization of such messages.
     */
    private final MessageSource messageSource;

    @Autowired
    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * The{@link ValidationException} throw in the service layer, it's custom exception for it if
     * validation data failed
     *
     * @param exception ValidationException
     * @param locale    the existing locale
     * @return the Response entity with an error code and a message.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorHandler> handleValidationException(ValidationException exception, Locale locale) {
        String message = messageSource.getMessage(exception.getMessageKey(), new Object[]{exception.getMessageValue()},
                locale);
        int errorCode = getErrorCodeBadRequest(exception.getIdentification());
        ErrorHandler errorHandler = new ErrorHandler(message, errorCode);
        LOGGER.log(Level.ERROR, "ValidationException message: {}", message);
        return new ResponseEntity<>(errorHandler, HttpStatus.BAD_REQUEST);
    }

    private int getErrorCodeBadRequest(IdentifierEntity identifierEntity) {
        int errorCode;
        switch (identifierEntity) {
            case TAG:
                errorCode = ErrorCode.BAD_REQUEST_TAG;
                break;
            case CERTIFICATE:
                errorCode = ErrorCode.BAD_REQUEST_CERTIFICATE;
                break;
            case USER:
                errorCode = ErrorCode.BAD_REQUEST_USER;
                break;
            default:
                errorCode = ErrorCode.BAD_REQUEST_ALL;
        }
        return errorCode;
    }

    /**
     * The{@link ResourceNotFoundException} throw in the service layer, it's custom exception for it if
     * resources not found
     *
     * @param exception if  resources not found
     * @param locale    the existing locale
     * @return the Response entity with an error code and a message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorHandler> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        Locale locale) {
        String message = messageSource.getMessage(exception.getMessageKey(), new Object[]{exception.getMessageValue()},
                locale);
        int errorCode = getErrorCodeNotFound(exception.getIdentification());
        ErrorHandler errorHandler = new ErrorHandler(message, errorCode);
        LOGGER.log(Level.ERROR, "ResourceNotFoundException message: {}", message);
        return new ResponseEntity<>(errorHandler, HttpStatus.NOT_FOUND);
    }


    private int getErrorCodeNotFound(IdentifierEntity identifierEntity) {
        int errorCode;
        switch (identifierEntity) {
            case TAG:
                errorCode = ErrorCode.NOT_FOUND_TAG;
                break;
            case CERTIFICATE:
                errorCode = ErrorCode.NOT_FOUND_CERTIFICATE;
                break;
            case USER:
                errorCode = ErrorCode.NOT_FOUND_USER;
                break;
            default:
                errorCode = ErrorCode.NOT_FOUND_ALL;
        }
        return errorCode;
    }

    /**
     * Exception thrown on a type mismatch when trying to set a bean property.
     *
     * @param exception TypeMismatchException
     * @param locale    the existing locale
     * @return the Response entity with an error code and a message.
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorHandler> handleTypeMismatchException(TypeMismatchException exception, Locale locale) {
        String message = messageSource.getMessage(INCORRECT_TYPE, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.BAD_REQUEST_ALL);
        LOGGER.log(Level.ERROR, "TypeMismatchException message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.BAD_REQUEST);
    }

    /**
     * This is a custom solution to solve 404 responses
     * By default when the DispatcherServlet can't find a handler for a request it sends a 404 response.
     * However if its property "throwExceptionIfNoHandlerFound" is set to true this exception is raised and
     * may be handled with a configured HandlerExceptionResolver.
     *
     * @param exception the NoHandlerFoundException exception
     * @param locale    the existing locale
     * @return the Response entity with an error code and a message.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorHandler> handleNoHandlerFoundException(NoHandlerFoundException exception, Locale locale) {
        String message = messageSource.getMessage(PAGE_NOT_FOUND, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.NOT_FOUND_ALL);
        LOGGER.log(Level.ERROR, "NoHandlerFoundException message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.NOT_FOUND);
    }

    /**
     * Exception thrown when a request handler does not support a specific request method.
     *
     * @param exception the HttpRequestMethodNotSupportedException exception
     * @param locale    the existing locale
     * @return the Response entity with an error code and a message.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorHandler> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception, Locale locale) {
        String message = messageSource.getMessage(METHOD_NOT_SUPPORT, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.METHOD_NOT_ALLOWED);
        LOGGER.log(Level.ERROR, "HttpRequestMethodNotSupportedException message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * The handle Exception  when a request handler does not support a specific request method.
     *
     * @param exception HttpMessageNotReadableException
     * @param locale    the existing locale
     * @return the Response entity with an error code and a message.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorHandler> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception, Locale locale) {
        String message = messageSource.getMessage(BODY_MISSING, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.BAD_REQUEST_ALL);
        LOGGER.log(Level.ERROR, "HttpMessageNotReadableException message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.BAD_REQUEST);
    }

    /**
     * The handle all internal exceptions
     *
     * @param exception -the Exception
     * @param locale    the existing locale
     * @return the Response entity with an error code and a message.
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorHandler> handleException(Exception exception, Locale locale) {
        String message = messageSource.getMessage(INTERNAL_ERROR, null, locale);
        ErrorHandler errorHandler = new ErrorHandler(message, ErrorCode.INTERNAL_ERROR);
        LOGGER.log(Level.ERROR, "Exception message: {}", exception.getMessage());
        return new ResponseEntity<>(errorHandler, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
