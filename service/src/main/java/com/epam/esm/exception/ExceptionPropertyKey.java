package com.epam.esm.exception;

/**
 * The class represents keys for exceptions in service layer.
 *
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
    public static final String EXISTING_CERTIFICATE = "existInTheDatabase";
    public static final String EXISTING_TAG = "existTagInTheDatabase";
    public static final String INCORRECT_PAGE = "incorrect.page";
    public static final String INCORRECT_MAX_PAGE = "incorrect.maxPage";
    public static final String INCORRECT_FIELD = "incorrect.field";

    private ExceptionPropertyKey() {
    }
}
