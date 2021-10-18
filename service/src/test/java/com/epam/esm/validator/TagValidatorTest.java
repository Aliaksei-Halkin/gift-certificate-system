package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Aliaksei Halkin
 */
class TagValidatorTest {
    private final TagValidator tagValidator = new TagValidator();

    public static Object[][] correctTag() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("rest 1");
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("danger! 2");
        Tag tag3 = new Tag();
        tag3.setId(3L);
        tag3.setName("skyDiving? 3");
        Tag tag4 = new Tag();
        tag4.setId(4L);
        tag4.setName("winter");
        return new Object[][]{{tag1}, {tag2}, {tag3}, {tag4}};
    }

    @ParameterizedTest
    @MethodSource("correctTag")
    void whenIsValidNameTagThenShouldNotThrowException(Tag tag) {
        assertDoesNotThrow(() -> tagValidator.isValidTag(tag));
    }

    public static Object[][] incorrectTag() {
        Tag tagDto1 = new Tag();
        tagDto1.setId(1L);
        tagDto1.setName("rest_1*");
        Tag tagDto2 = new Tag();
        tagDto2.setId(1L);
        tagDto2.setName("danger!_2/");
        Tag tagDto3 = new Tag();
        tagDto3.setId(1L);
        tagDto3.setName("skyDiving?_3&");
        Tag tagDto4 = new Tag();
        tagDto4.setId(1L);
        tagDto4.setName("лето256>");
        Tag tagDto5 = new Tag();
        tagDto5.setId(1L);
        tagDto5.setName("");
        return new Object[][]{{tagDto1}, {tagDto2}, {tagDto3}, {tagDto4}, {tagDto5}};
    }

    @ParameterizedTest
    @MethodSource("incorrectTag")
    void whenIsNotValidNameTagThenShouldThrowException(Tag tag) {
        assertThrows(ValidationException.class, () -> tagValidator.isValidTag(tag));
    }

    public static Object[][] correctId() {
        return new Object[][]{{25L}, {258L}, {123456789L}, {13L}};
    }

    @ParameterizedTest
    @MethodSource("correctId")
    void whenIsValidIdTagThenShouldNotThrowException(Long id) {
        assertDoesNotThrow(() -> tagValidator.isValidId(id));
    }

    public static Object[][] incorrectId() {
        return new Object[][]{{0L}, {-1L}, {-123456789L}, {-13L}};
    }

    @ParameterizedTest
    @MethodSource("incorrectId")
    void whenIsNotValidIdTagThenShouldNotThrowException(Long id) {
        assertThrows(ValidationException.class, () -> tagValidator.isValidId(id));
    }

}