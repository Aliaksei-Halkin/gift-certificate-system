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
    public static final String INCORRECT_FIELD_VALUE = "incorrect.field.value";
    public static final String USER_WITH_ID_NOT_FOUND = "user.id.not.found";
    public static final String USER_ORDER_NOT_FOUND = "user.order.not.found";;
    public static final String INCORRECT_QUANTITY_IN_ORDER = "incorrect.quantity.order";
    public static final String GIFT_CERTIFICATES_NOT_FOUND = "certificates.notFound";
    public static final String ORDER_WITH_ID_NOT_FOUND = "order.not.found";
    public static final String NOT_NULL_ID = "not.null.id";
    public static final String ACTIVE_FALSE = "active.false";
    public static final String INCORRECT_LOGIN = "incorrect.login";
    public static final String INCORRECT_PASSWORD = "incorrect.password";


    private ExceptionPropertyKey() {
    }
}
