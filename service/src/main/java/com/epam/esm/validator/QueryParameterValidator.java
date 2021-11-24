package com.epam.esm.validator;

import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ValidationException;

import java.util.Map;

/**
 * The QueryParameterValidator class represents query parameter data validation
 *
 * @author Aliaksei Halkin
 */
public class QueryParameterValidator {
    private static final String REGEX_TAG_NAME = "[а-яА-Я\\w\\s\\.,?!']{1,45}";
    private static final String REGEX_TAG_NAME_KEY = "tagName([1-9]\\d*)?";
    private static final String REGEX_GIFT_CERTIFICATE_NAME_AND_DESCRIPTION = "[а-яА-Я\\w\\s\\.,?!']{1,250}";
    private static final String REGEX_GIFT_CERTIFICATE_NAME_KEY = "name";
    private static final String REGEX_GIFT_CERTIFICATE_DESCRIPTION_KEY = "description";
    private static final String REGEX_PAGE_KEY = "page";
    private static final String REGEX_PER_PAGE_KEY = "per_page";
    private static final String REGEX_PAGE_VALUE = "[1-9]\\d*";
    private static final String REGEX_ORDER_VALUE = "[-]?name|[-]?description";
    private static final String REGEX_ORDER_KEY = "order";

    private QueryParameterValidator() {
    }

    public static void isValidGiftCertificateQueryParameters(Map<String, String> queryParameters) {
        queryParameters.forEach((key, value) -> {
            if (key.matches(REGEX_TAG_NAME_KEY)) {
                isValidTagName(value);
            }
        });
        isValidGiftCertificateName(queryParameters.get(REGEX_GIFT_CERTIFICATE_NAME_KEY));
        isValidGiftCertificateDescription(queryParameters.get(REGEX_GIFT_CERTIFICATE_DESCRIPTION_KEY));
        isValidOrderType(queryParameters.get(REGEX_ORDER_KEY));
        isValidPage(queryParameters.get(REGEX_PAGE_KEY));
        isValidPage(queryParameters.get(REGEX_PER_PAGE_KEY));
    }

    private static void isValidTagName(String tagName) {
        if (tagName != null && !tagName.isEmpty() && !tagName.matches(REGEX_TAG_NAME)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_TAG_NAME, tagName, IdentifierEntity.TAG);
        }
    }

    private static void isValidGiftCertificateName(String giftCertificateName) {
        if (giftCertificateName != null && !giftCertificateName.isEmpty()
                && !giftCertificateName.matches(REGEX_GIFT_CERTIFICATE_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_NAME, giftCertificateName,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    private static void isValidGiftCertificateDescription(String giftCertificateDescription) {
        if (giftCertificateDescription != null && !giftCertificateDescription.isEmpty()
                && !giftCertificateDescription.matches(REGEX_GIFT_CERTIFICATE_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_DESCRIPTION,
                    giftCertificateDescription, IdentifierEntity.CERTIFICATE);
        }
    }

    private static void isValidOrderType(String order) {
        if (order != null && !order.isEmpty() && !order.matches(REGEX_ORDER_VALUE)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_ORDER, order,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    public static void isValidPage(String page) {
        if (page != null && !page.isEmpty() && !page.matches(REGEX_PAGE_VALUE)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_PAGE, page, IdentifierEntity.CERTIFICATE);
        }
        if (page == null || page.isEmpty()) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_PAGE, page, IdentifierEntity.CERTIFICATE);
        }
    }
}
