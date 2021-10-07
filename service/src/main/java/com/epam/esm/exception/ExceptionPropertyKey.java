package com.epam.esm.exception;

/**
 * @author Aliaksei Halkin
 */
public class ExceptionPropertyKey {
    public static final String TAG_WITH_NAME_NOT_FOUND = "tagWithNameNotFound";
    public static final String TAG_WITH_ID_NOT_FOUND = "tagWithIdNotFound";
    public static final String GIFT_CERTIFICATE_WITH_ID_NOT_FOUND = "giftCertificateWithIdNotFound";
    public static final String INCORRECT_ID = "incorrectId";
    public static final String INCORRECT_GIFT_CERTIFICATE_NAME = "incorrectGiftCertificateName";
    public static final String INCORRECT_TAG_NAME = "incorrectTagName";
    public static final String INCORRECT_GIFT_CERTIFICATE_DESCRIPTION = "incorrectGiftCertificateDescription";
    public static final String INCORRECT_PRICE = "incorrectPrice";
    public static final String INCORRECT_DURATION = "incorrectDuration";
    public static final String INCORRECT_ORDER = "incorrectOrder";
    public static final String INCORRECT_DIRECTION = "incorrectDirection";

    private ExceptionPropertyKey() {
    }
}
