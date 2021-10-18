package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * The GiftCertificateValidator class represents certificate data validation
 *
 * @author Aliaksei Halkin
 */
@Component
public class GiftCertificateValidator {
    private static final String REGEX_NAME_AND_DESCRIPTION = "[à-ÿÀ-ß\\w\\s\\.,?!']{1,250}";
    private static final long MIN_ID = 1;
    private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");
    private static final BigDecimal MAX_PRICE = new BigDecimal("1000000");
    private static final int MIN_DURATION = 1;
    private static final int MAX_DURATION = 365;

    public GiftCertificateValidator() {
    }

    public void isValidGiftCertificate(GiftCertificate giftCertificate) {
        isValidName(giftCertificate.getName());
        isValidDescription(giftCertificate.getDescription());
        isValidPrice(giftCertificate.getPrice());
        isValidDuration(giftCertificate.getDuration());
    }

    public void isValidId(long id) {
        if (id < MIN_ID) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_ID, id);
        }
    }

    private void isValidName(String name) {
        if (name == null || name.isEmpty() || !name.matches(REGEX_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_NAME, name);
        }
    }

    private void isValidDescription(String description) {
        if (description != null && !description.isEmpty() && !description.matches(REGEX_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_DESCRIPTION, description);
        }
    }

    private void isValidPrice(BigDecimal price) {
        if (price == null || price.compareTo(MIN_PRICE) < 0 || price.compareTo(MAX_PRICE) > 0) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_PRICE, price);
        }
    }

    private void isValidDuration(int duration) {
        if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_DURATION, duration);
        }
    }
}
