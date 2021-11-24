package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;
import com.epam.esm.util.ParameterManager;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Aliaksei Halkin
 */
class QueryParameterValidatorTest {
    public static Object[][] correctQueryParameter() {
        Map<String, String> queryParameter1 = new HashMap<>();
        queryParameter1.put("name", "Hello2");
        queryParameter1.put("tagName", "Hello, test,fresh");
        queryParameter1.put("description", "Movies");
        queryParameter1.put("order", "-Name");
        queryParameter1.put("page", "1");
        queryParameter1.put("per_page", "1");
        Map<String, String> queryParameter2 = new HashMap<>();
        queryParameter2.put("name", "Cinema2");
        queryParameter2.put("tagName", "NEw , null,0000,test,fresh");
        queryParameter2.put("description", "Description2");
        queryParameter2.put("order", " name");
        queryParameter2.put("page", "15");
        queryParameter2.put("per_page", "10");
        Map<String, String> queryParameter3 = new HashMap<>();
        queryParameter3.put("name", "Hello3");
        queryParameter3.put("tagName", "Hello");
        queryParameter3.put("order", "-deScription");
        queryParameter2.put("description", "Description2 with me");
        queryParameter3.put("page", "5");
        queryParameter3.put("per_page", "100");
        Map<String, String> queryParameter4 = new HashMap<>();
        queryParameter4.put("order", "-description");
        queryParameter2.put("name", "Cinema2");
        queryParameter2.put("tagName", "NEw , null,0000,test,fresh");
        queryParameter2.put("description", "Description2");
        queryParameter4.put("page", "5");
        queryParameter4.put("per_page", "100");
        Map<String, String> queryParameter5 = new HashMap<>();
        queryParameter3.put("tagName", "Hello");
        queryParameter5.put("description", "Go to the cinema");
        queryParameter5.put("order", "description");
        queryParameter5.put("page", "1045");
        queryParameter5.put("per_page", "10");
        Map<String, String> queryParameter6 = new HashMap<>();
        queryParameter6.put("page", "1045");
        queryParameter6.put("per_page", "10");
        return new Object[][]{
                {queryParameter5},
                {queryParameter5},
                {queryParameter5},
                {queryParameter5},
                {queryParameter5},
                {queryParameter6}};
    }

    @ParameterizedTest
    @MethodSource("correctQueryParameter")
    void when_IsValidQueryParameters_ThenShould_NotThrowException(Map<String, String> queryParameter) {
        assertDoesNotThrow(() ->
                QueryParameterValidator.isValidGiftCertificateQueryParameters(queryParameter));
    }

    public static Object[][] incorrectQueryParameter() {
        Map<String, String> queryParameter1 = new HashMap<>();
        queryParameter1.put("name", "Hello2/");
        queryParameter1.put("tagName", "Hello*, test,<alert>");
        queryParameter1.put("description", "<alert>");
        queryParameter1.put("order", " <alert>");
        queryParameter1.put("page", "0");
        queryParameter1.put("per_page", "-1");
        Map<String, String> queryParameter2 = new HashMap<>();
        queryParameter2.put("name", "/");
        queryParameter2.put("tagName", "// ,  ,test,fresh");
        queryParameter2.put("description", "-new");
        queryParameter2.put("order", " tagName+tag");
        queryParameter2.put("page", "page");
        queryParameter2.put("per_page", "10");
        Map<String, String> queryParameter3 = new HashMap<>();
        queryParameter3.put("name", "Hel   ");
        queryParameter3.put("tagName", "Hello7/24");
        queryParameter3.put("order", "-desc");
        queryParameter2.put("description", "Description2 with me");
        queryParameter3.put("page", "0");
        queryParameter3.put("per_page", "100");
        Map<String, String> queryParameter4 = new HashMap<>();
        queryParameter4.put("order", "-description");
        queryParameter2.put("name", "Cinema2");
        queryParameter2.put("tagName", "NEw , null,0000,test,fresh");
        queryParameter2.put("description", "Description2");
        queryParameter4.put("page", "5.5");
        queryParameter4.put("per_page", "100");
        Map<String, String> queryParameter5 = new HashMap<>();
        queryParameter3.put("tagName", "Hello");
        queryParameter5.put("description", "Go to the cinema");
        queryParameter5.put("order", "description");
        queryParameter5.put("page", "10+");
        queryParameter5.put("per_page", "10.5");
        Map<String, String> queryParameter6 = new HashMap<>();
        queryParameter6.put("page", "-1045");
        queryParameter6.put("per_page", "10,8");
        return new Object[][]{
                {queryParameter5},
                {queryParameter5},
                {queryParameter5},
                {queryParameter5},
                {queryParameter5},
                {queryParameter6}};
    }

    @ParameterizedTest
    @MethodSource("incorrectQueryParameter")
    void whenIsNotValidQueryParametersThenShouldThrowException(Map<String,String> parameters) {
        assertThrows(ValidationException.class, () -> QueryParameterValidator.isValidGiftCertificateQueryParameters(parameters));
    }
}