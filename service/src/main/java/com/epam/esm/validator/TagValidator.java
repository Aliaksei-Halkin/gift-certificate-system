package com.epam.esm.validator;

import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ValidationException;
import org.springframework.stereotype.Component;

/**
 * The TagValidator class represents tag data validation
 *
 * @author Aliaksei Halkin
 */
@Component
public class TagValidator {
    private static final long MIN_ID = 1;
    private static final String REGEX_NAME = "[а-яА-Я\\w\\s\\.,?!']{1,45}";

    public void isValidTag(TagEntity tag) {
        isValidName(tag.getName());
    }

    public void isValidId(long id) {
//        if (id < MIN_ID) {
//            throw new ValidationException(ExceptionPropertyKey.INCORRECT_ID, id, IdentifierEntity.TAG);
//        }
    }

    private void isValidName(String name) {
//        if (name == null || name.isEmpty() || !name.matches(REGEX_NAME)) {
//            throw new ValidationException(ExceptionPropertyKey.INCORRECT_TAG_NAME, name, IdentifierEntity.TAG);
//        }
    }
}
