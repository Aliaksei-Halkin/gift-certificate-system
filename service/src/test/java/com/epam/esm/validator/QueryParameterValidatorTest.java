package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;
import com.epam.esm.util.ParameterManager;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Aliaksei Halkin
 */
class QueryParameterValidatorTest {
    public static Object[][] correctQueryParameter() {
        QueryParameter queryParameter1 =
                new QueryParameter("Hello1", "Cinema1", "Description1", "name");
        QueryParameter queryParameter2 =
                new QueryParameter("Hello2", "Cinema2", "Description2", "name");
        QueryParameter queryParameter3 =
                new QueryParameter("Hello3", "Cinema3", "Description3", null);
        QueryParameter queryParameter4 =
                new QueryParameter("Hello4", "Cinema4", null, null);
        QueryParameter queryParameter5 =
                new QueryParameter("Hello4", null, null, null);
        QueryParameter queryParameter6 =
                new QueryParameter(null, null, null, null);
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
        Map<String, String> parameter = ParameterManager.convertQueryParameterToMap(queryParameter);
        assertDoesNotThrow(() ->
                QueryParameterValidator.isValidGiftCertificateQueryParameters(parameter));
    }

    public static Object[][] incorrectQueryParameter() {
        QueryParameter queryParameter1 =
                new QueryParameter("@312", "Cinema1", "Description1", "name");
        QueryParameter queryParameter2 =
                new QueryParameter("Hello1", "@3??.123##!@", "Description1", "name");
        QueryParameter queryParameter3 =
                new QueryParameter("Hello1", "Cinema1", "$%^%^^", "name");
        QueryParameter queryParameter4 =
                new QueryParameter("Hello1", "Cinema1", "Description1", "asdd");
        QueryParameter queryParameter5 =
                new QueryParameter("Hello1", "Cinema1", "Description1", "name");
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
        Map<String, String> parameter = ParameterManager.convertQueryParameterToMap(queryParameter);
        assertThrows(ValidationException.class, () -> QueryParameterValidator.isValidGiftCertificateQueryParameters(parameter));
    }

}