package com.epam.esm.validator;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
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


    public void isValidGiftCertificate(GiftCertificate giftCertificate) {
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

    private void isValidName(String name) {
        if (name == null || name.isEmpty() || !name.matches(REGEX_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_NAME, name,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    public  Optional<GiftCertificate> ifExistName(String name) {
        Optional<GiftCertificate> certificateByName = giftCertificateDao.findByName(name);
        if(certificateByName.isPresent()&&certificateByName.get().isActive()==true){
            throw new ValidationException(ExceptionPropertyKey.EXISTING_CERTIFICATE, name,
                   IdentifierEntity.CERTIFICATE);
        }
        return certificateByName;
    }

    private void isValidDescription(String description) {
        if (description != null && !description.isEmpty() && !description.matches(REGEX_NAME_AND_DESCRIPTION)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_DESCRIPTION, description,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    private void isValidPrice(BigDecimal price) {
        if (price == null || price.compareTo(MIN_PRICE) < 0 || price.compareTo(MAX_PRICE) > 0) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_PRICE, price,
                    IdentifierEntity.CERTIFICATE);
        }
    }

    private void isValidDuration(int duration) {
        if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_DURATION, duration,
                    IdentifierEntity.CERTIFICATE);
        }
    }
}
