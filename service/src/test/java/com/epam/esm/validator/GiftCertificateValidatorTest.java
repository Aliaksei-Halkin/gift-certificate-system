package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliaksei Halkin
 */
class GiftCertificateValidatorTest {

    public static Object[][] correctGiftCertificate() {
        GiftCertificateDto giftCertificate1 = new GiftCertificateDto();
        giftCertificate1.setName("Hello");
        giftCertificate1.setDescription("Hello from description");
        giftCertificate1.setDuration(1);
        giftCertificate1.setPrice(new BigDecimal("23.3"));

        GiftCertificateDto giftCertificate2 = new GiftCertificateDto();
        giftCertificate2.setName("Hello 2");
        giftCertificate2.setDescription("Hello from description 2");
        giftCertificate2.setDuration(2);
        giftCertificate2.setPrice(new BigDecimal("34"));

        GiftCertificateDto giftCertificate3 = new GiftCertificateDto();
        giftCertificate3.setName("Hello 3");
        giftCertificate3.setDescription("Hello from description 3");
        giftCertificate3.setDuration(2);
        giftCertificate3.setPrice(new BigDecimal("23"));
        return new Object[][]{{giftCertificate1}, {giftCertificate2}, {giftCertificate3}};
    }

    @ParameterizedTest
    @MethodSource("correctGiftCertificate")
    void whenIsValidGiftCertificateThenShouldNotThrowException(GiftCertificateDto giftCertificateDto) {
        assertDoesNotThrow(() -> GiftCertificateValidator.isValidGiftCertificate(giftCertificateDto));
    }

    public static Object[][] incorrectGiftCertificate() {
        GiftCertificateDto giftCertificate1 = new GiftCertificateDto();
        giftCertificate1.setName("");
        giftCertificate1.setDescription("Hello from description");
        giftCertificate1.setDuration(1);
        giftCertificate1.setPrice(new BigDecimal("23.3"));

        GiftCertificateDto giftCertificate2 = new GiftCertificateDto();
        giftCertificate2.setName("Hello 2");
        giftCertificate2.setDescription("@@");
        giftCertificate2.setDuration(2);
        giftCertificate2.setPrice(new BigDecimal("34"));

        GiftCertificateDto giftCertificate3 = new GiftCertificateDto();
        giftCertificate3.setName("Hello 3");
        giftCertificate3.setDescription("Hello from description 3");
        giftCertificate3.setDuration(0);
        giftCertificate3.setPrice(new BigDecimal("23"));

        GiftCertificateDto giftCertificate4 = new GiftCertificateDto();
        giftCertificate4.setName("Hello 3");
        giftCertificate4.setDescription("Hello from description 3");
        giftCertificate4.setDuration(2);
        giftCertificate4.setPrice(new BigDecimal(-1450));
        return new Object[][]{{giftCertificate1}, {giftCertificate2}, {giftCertificate3}, {giftCertificate4}};
    }

    @ParameterizedTest
    @MethodSource("incorrectGiftCertificate")
    void whenIsNotValidGiftCertificateThenShouldThrowException(GiftCertificateDto giftCertificateDto) {
        assertThrows(ValidationException.class, () -> GiftCertificateValidator.isValidGiftCertificate(giftCertificateDto));
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
