package com.epam.esm.validator;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Aliaksei Halkin
 */
class TagValidatorTest {
    public static Object[][] correctTag() {
        TagDto tagDto1 = new TagDto();
        tagDto1.setId(1L);
        tagDto1.setName("rest 1");
        TagDto tagDto2 = new TagDto();
        tagDto2.setId(2L);
        tagDto2.setName("danger! 2");
        TagDto tagDto3 = new TagDto();
        tagDto3.setId(3L);
        tagDto3.setName("skyDiving? 3");
        TagDto tagDto4 = new TagDto();
        tagDto4.setId(4L);
        tagDto4.setName("winter");
        return new Object[][]{{tagDto1}, {tagDto2}, {tagDto3}, {tagDto4}};
    }

    @ParameterizedTest
    @MethodSource("correctTag")
    void whenIsValidNameTagThenShouldNotThrowException(TagDto tagDto) {
        assertDoesNotThrow(() -> TagValidator.isValidTag(tagDto));
    }

    public static Object[][] incorrectTag() {
        TagDto tagDto1 = new TagDto();
        tagDto1.setId(1L);
        tagDto1.setName("rest_1*");
        TagDto tagDto2 = new TagDto();
        tagDto2.setId(1L);
        tagDto2.setName("danger!_2/");
        TagDto tagDto3 = new TagDto();
        tagDto3.setId(1L);
        tagDto3.setName("skyDiving?_3&");
        TagDto tagDto4 = new TagDto();
        tagDto4.setId(1L);
        tagDto4.setName("лето256>");
        TagDto tagDto5 = new TagDto();
        tagDto5.setId(1L);
        tagDto5.setName("");
        return new Object[][]{{tagDto1}, {tagDto2}, {tagDto3}, {tagDto4}, {tagDto5}};
    }

    @ParameterizedTest
    @MethodSource("incorrectTag")
    void whenIsNotValidNameTagThenShouldThrowException(TagDto tagDto) {
        assertThrows(ValidationException.class, () -> TagValidator.isValidTag(tagDto));
    }

    public static Object[][] correctId() {
        return new Object[][]{{25L}, {258L}, {123456789L}, {13L}};
    }

    @ParameterizedTest
    @MethodSource("correctId")
    void whenIsValidIdTagThenShouldNotThrowException(Long id) {
        assertDoesNotThrow(() -> TagValidator.isValidId(id));
    }

    public static Object[][] incorrectId() {
        return new Object[][]{{0L}, {-1L}, {-123456789L}, {-13L}};
    }

    @ParameterizedTest
    @MethodSource("incorrectId")
    void whenIsNotValidIdTagThenShouldNotThrowException(Long id) {
        assertThrows(ValidationException.class, () -> TagValidator.isValidId(id));
    }

}