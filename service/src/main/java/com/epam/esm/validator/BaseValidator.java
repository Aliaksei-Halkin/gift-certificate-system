package com.epam.esm.validator;

public abstract class BaseValidator {

    public abstract void checkForActive(Boolean active);

    public abstract void checkForNoId(Long id);

    public abstract void isValidId(long id);
}
