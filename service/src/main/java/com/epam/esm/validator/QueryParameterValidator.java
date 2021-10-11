package com.epam.esm.validator;

import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.util.QueryParameter;

/**
 * @author Aliaksei Halkin
 */
public class QueryParameterValidator {
    private static final String REGEX_TAG_NAME = "[\\w\\s\\.,?!]{1,45}";
    private static final String REGEX_GIFT_CERTIFICATE_NAME_AND_DESCRIPTION = "[а-яА-Я\\w\\s\\d\\.,?!']{1,250}";
    private static final String REGEX_ORDER = "name|description";
    private static final String REGEX_DIRECTION = "asc|desc";

    private QueryParameterValidator() {
    }

    public static void isValidQueryParameters(QueryParameter queryParameter) {
        isValidTagName(queryParameter.getTagName());
        isValidGiftCertificateName(queryParameter.getCertificateName());
        isValidGiftCertificateDescription(queryParameter.getCertificateDescription());
        isValidOrderType(queryParameter.getOrder());
        isValidSortDirection(queryParameter.getDirection());
    }

    private static void isValidTagName(String tagName) {
        if (tagName != null && !tagName.isEmpty() && !tagName.matches(REGEX_TAG_NAME)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_TAG_NAME, tagName);
        }
    }

    private static void isValidGiftCertificateName(String giftCertificateName) {
        if (giftCertificateName != null && !giftCertificateName.isEmpty()
                && !giftCertificateName.matches(REGEX_GIFT_CERTIFICATE_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_NAME, giftCertificateName);
        }
    }

    private static void isValidGiftCertificateDescription(String giftCertificateDescription) {
        if (giftCertificateDescription != null && !giftCertificateDescription.isEmpty()
                && !giftCertificateDescription.matches(REGEX_GIFT_CERTIFICATE_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_DESCRIPTION,
                    giftCertificateDescription);
        }
    }

    private static void isValidOrderType(String order) {
        if (order != null && !order.isEmpty() && !order.matches(REGEX_ORDER)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_ORDER, order);
        }
    }

    private static void isValidSortDirection(String direction) {
        if (direction != null && !direction.isEmpty() && !direction.matches(REGEX_DIRECTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_DIRECTION, direction);
        }
    }
}
