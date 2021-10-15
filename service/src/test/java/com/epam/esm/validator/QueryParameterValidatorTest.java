package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;
import com.epam.esm.util.QueryParameter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliaksei Halkin
 */
class QueryParameterValidatorTest {
    public static Object[][] correctQueryParameter() {
        QueryParameter queryParameter1 =
                new QueryParameter("Hello1", "Cinema1", "Description1", "name", "asc");
        QueryParameter queryParameter2 =
                new QueryParameter("Hello2", "Cinema2", "Description2", "name", null);
        QueryParameter queryParameter3 =
                new QueryParameter("Hello3", "Cinema3", "Description3", null, null);
        QueryParameter queryParameter4 =
                new QueryParameter("Hello4", "Cinema4", null, null, null);
        QueryParameter queryParameter5 =
                new QueryParameter("Hello4", null, null, null, null);
        QueryParameter queryParameter6 =
                new QueryParameter(null, null, null, null, null);
        return new Object[][]{
                {queryParameter1},
                {queryParameter2},
                {queryParameter3},
                {queryParameter4},
                {queryParameter5},
                {queryParameter6}};
    }

    @ParameterizedTest
    @MethodSource("correctQueryParameter")
    void whenIsValidQueryParametersThenShouldNotThrowException(QueryParameter queryParameter) {
        assertDoesNotThrow(() -> QueryParameterValidator.isValidQueryParameters(queryParameter));
    }

    public static Object[][] incorrectQueryParameter() {
        QueryParameter queryParameter1 =
                new QueryParameter("@312", "Cinema1", "Description1", "name", "asc");
        QueryParameter queryParameter2 =
                new QueryParameter("Hello1", "@3??.123##!@", "Description1", "name", "asc");
        QueryParameter queryParameter3 =
                new QueryParameter("Hello1", "Cinema1", "$%^%^^", "name", "asc");
        QueryParameter queryParameter4 =
                new QueryParameter("Hello1", "Cinema1", "Description1", "asdd", "asc");
        QueryParameter queryParameter5 =
                new QueryParameter("Hello1", "Cinema1", "Description1", "name", "asdasd");
        return new Object[][]{
                {queryParameter1},
                {queryParameter2},
                {queryParameter3},
                {queryParameter4},
                {queryParameter5}};
    }

    @ParameterizedTest
    @MethodSource("incorrectQueryParameter")
    void whenIsNotValidQueryParametersThenShouldThrowException(QueryParameter queryParameter) {
        assertThrows(ValidationException.class, () -> QueryParameterValidator.isValidQueryParameters(queryParameter));
    }

}