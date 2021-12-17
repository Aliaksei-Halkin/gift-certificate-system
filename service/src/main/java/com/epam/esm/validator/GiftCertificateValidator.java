package com.epam.esm.validator;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateField;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The GiftCertificateValidator class represents certificate data validation
 *
 * @author Aliaksei Halkin
 */
@Component
public class GiftCertificateValidator {
    private static final String REGEX_NAME_AND_DESCRIPTION = "[а-яА-Я\\w\\s\\.,?!'-]{1,250}";
    private static final long MIN_ID = 1;
    private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");
    private static final BigDecimal MAX_PRICE = new BigDecimal("1000000");
    private static final int MIN_DURATION = 1;
    private static final int MAX_DURATION = 365;
    public static final String FIND_BY_NAME = " WHERE name = '";
    private final GiftCertificateDao giftCertificateDao;

    @Autowired
    public GiftCertificateValidator(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }


    public void isValidGiftCertificate(GiftCertificateEntity giftCertificate) {
        isValidName(giftCertificate.getName());
        isValidDescription(giftCertificate.getDescription());
        isValidPrice(giftCertificate.getPrice());
        isValidDuration(giftCertificate.getDuration());
    }

    public void isValidId(long id) {
        if (id < MIN_ID) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_ID, id,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    public void isValidName(String name) {
        if (name == null || name.isEmpty() || !name.matches(REGEX_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_NAME, name,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    public Optional<GiftCertificateEntity> ifExistName(String name) {
        Optional<GiftCertificateEntity> certificateByName = giftCertificateDao.findByName(name);
        if (certificateByName.isPresent() && certificateByName.get().isActive() == true) {
            throw new ValidationException(ExceptionPropertyKey.EXISTING_CERTIFICATE, name,
                    IdentifierEntity.CERTIFICATE);
        }
        return certificateByName;
    }

    public void isValidDescription(String description) {
        if (description != null && !description.isEmpty() && !description.matches(REGEX_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_DESCRIPTION, description,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    public void isValidPrice(BigDecimal price) {
        if (price == null || price.compareTo(MIN_PRICE) < 0 || price.compareTo(MAX_PRICE) > 0) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_PRICE, price,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    public void isValidDuration(int duration) {
        if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_DURATION, duration,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    public void isValidField(GiftCertificateField giftCertificateField) {
        try {
            GiftCertificateField.FieldName fieldName = GiftCertificateField.FieldName.valueOf(giftCertificateField.getFieldName().toUpperCase());
            switch (fieldName) {
                case NAME:
                    isValidName(giftCertificateField.getFieldValue());
                    ifExistName(giftCertificateField.getFieldValue());
                    break;
                case DESCRIPTION:
                    isValidDescription(giftCertificateField.getFieldValue());
                    break;
                case PRICE:
                    isValidPrice(new BigDecimal(giftCertificateField.getFieldValue()));
                    break;
                case DURATION:
                    isValidDuration(Integer.parseInt(giftCertificateField.getFieldValue()));
                    break;
            }
        } catch (NumberFormatException exception) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_FIELD_VALUE,
                    giftCertificateField.getFieldName(), IdentifierEntity.CERTIFICATE);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_FIELD, giftCertificateField.getFieldName(),
                    IdentifierEntity.CERTIFICATE);
        }
    }
}
