package com.epam.esm.validator;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.ValidationException;

/**
 * @author Aliaksei Halkin
 */
public class TagValidator {
    private static final long MIN_ID = 1;
    private static final String REGEX_NAME = "[а-яА-Я\\w\\s\\.,?!']{1,45}";

    public static void isValidTag(TagDto tagDto) {
        isValidName(tagDto.getName());
    }

    public static void isValidId(long id) {
        if (id < MIN_ID) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_ID, id);
        }
    }

    private static void isValidName(String name) {
        if (name == null || name.isEmpty() || !name.matches(REGEX_NAME)) {
            throw new ValidationException(ExceptionPropertyKey.INCORRECT_TAG_NAME, name);
        }
    }
}
