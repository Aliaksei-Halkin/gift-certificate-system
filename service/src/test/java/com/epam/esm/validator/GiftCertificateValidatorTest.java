package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Aliaksei Halkin
 */
class GiftCertificateValidatorTest {

    public static Object[][] correctGiftCertificate() {
        GiftCertificate giftCertificate1 = new GiftCertificate();
        giftCertificate1.setName("Hello");
        giftCertificate1.setDescription("Hello from description");
        giftCertificate1.setDuration(1);
        giftCertificate1.setPrice(new BigDecimal("23.3"));

        GiftCertificate giftCertificate2 = new GiftCertificate();
        giftCertificate2.setName("Hello 2");
        giftCertificate2.setDescription("Hello from description 2");
        giftCertificate2.setDuration(2);
        giftCertificate2.setPrice(new BigDecimal("34"));

        GiftCertificate giftCertificate3 = new GiftCertificate();
        giftCertificate3.setName("Hello 3");
        giftCertificate3.setDescription("Hello from description 3");
        giftCertificate3.setDuration(2);
        giftCertificate3.setPrice(new BigDecimal("23"));
        return new Object[][]{{giftCertificate1}, {giftCertificate2}, {giftCertificate3}};
    }

    @ParameterizedTest
    @MethodSource("correctGiftCertificate")
    void whenIsValidGiftCertificateThenShouldNotThrowException(GiftCertificate giftCertificate) {
        assertDoesNotThrow(() -> GiftCertificateValidator.isValidGiftCertificate(giftCertificate));
    }

    public static Object[][] incorrectGiftCertificate() {
        GiftCertificate giftCertificate1 = new GiftCertificate();
        giftCertificate1.setName("");
        giftCertificate1.setDescription("Hello from description");
        giftCertificate1.setDuration(1);
        giftCertificate1.setPrice(new BigDecimal("23.3"));

        GiftCertificate giftCertificate2 = new GiftCertificate();
        giftCertificate2.setName("Hello 2");
        giftCertificate2.setDescription("@@");
        giftCertificate2.setDuration(2);
        giftCertificate2.setPrice(new BigDecimal("34"));

        GiftCertificate giftCertificate3 = new GiftCertificate();
        giftCertificate3.setName("Hello 3");
        giftCertificate3.setDescription("Hello from description 3");
        giftCertificate3.setDuration(0);
        giftCertificate3.setPrice(new BigDecimal("23"));

        GiftCertificate giftCertificate4 = new GiftCertificate();
        giftCertificate4.setName("Hello 3");
        giftCertificate4.setDescription("Hello from description 3");
        giftCertificate4.setDuration(2);
        giftCertificate4.setPrice(new BigDecimal(-1450));
        return new Object[][]{{giftCertificate1}, {giftCertificate2}, {giftCertificate3}, {giftCertificate4}};
    }

    @ParameterizedTest
    @MethodSource("incorrectGiftCertificate")
    void whenIsNotValidGiftCertificateThenShouldThrowException(GiftCertificate giftCertificate) {
        assertThrows(ValidationException.class, () -> GiftCertificateValidator.isValidGiftCertificate(giftCertificate));
    }

    public static Object[][] correctId() {
        return new Object[][]{{1}, {25}, {103}};
    }

    @ParameterizedTest
    @MethodSource("correctId")
    void whenIsValidIdThenShouldNotThrowException(long id) {
        assertDoesNotThrow(() -> GiftCertificateValidator.isValidId(id));
    }

    public static Object[][] incorrectId() {
        return new Object[][]{{0}, {-25}, {Long.MIN_VALUE}};
    }

    @ParameterizedTest
    @MethodSource("incorrectId")
    void whenIsNotValidIdThenShouldThrowException(long id) {
        assertThrows(ValidationException.class, () -> GiftCertificateValidator.isValidId(id));
    }
}
