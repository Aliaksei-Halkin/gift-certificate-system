package com.epam.esm.validator;

import com.epam.esm.entity.TagEntity;
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
        TagEntity tag1 = new TagEntity();
        tag1.setId(1L);
        tag1.setName("rest 1");
        TagEntity tag2 = new TagEntity();
        tag2.setId(2L);
        tag2.setName("danger! 2");
        TagEntity tag3 = new TagEntity();
        tag3.setId(3L);
        tag3.setName("skyDiving? 3");
        TagEntity tag4 = new TagEntity();
        tag4.setId(4L);
        tag4.setName("winter");
        return new Object[][]{{tag1}, {tag2}, {tag3}, {tag4}};
    }

    @ParameterizedTest
    @MethodSource("correctTag")
    void whenIsValidNameTagThenShouldNotThrowException(TagEntity tag) {
        assertDoesNotThrow(() -> tagValidator.isValidTag(tag));
    }

    public static Object[][] incorrectTag() {
        TagEntity tagDto1 = new TagEntity();
        tagDto1.setId(1L);
        tagDto1.setName("rest_1*");
        TagEntity tagDto2 = new TagEntity();
        tagDto2.setId(1L);
        tagDto2.setName("danger!_2/");
        TagEntity tagDto3 = new TagEntity();
        tagDto3.setId(1L);
        tagDto3.setName("skyDiving?_3&");
        TagEntity tagDto4 = new TagEntity();
        tagDto4.setId(1L);
        tagDto4.setName("лето256>");
        TagEntity tagDto5 = new TagEntity();
        tagDto5.setId(1L);
        tagDto5.setName("");
        return new Object[][]{{tagDto1}, {tagDto2}, {tagDto3}, {tagDto4}, {tagDto5}};
    }

    @ParameterizedTest
    @MethodSource("incorrectTag")
    void whenIsNotValidNameTagThenShouldThrowException(TagEntity tag) {
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