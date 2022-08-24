package com.epam.esm.dto;

import com.epam.esm.exception.ExceptionPropertyKey;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class RegistrationUserDto {
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_LOGIN)
    @Pattern(regexp = "\\w{1,20}", message = ExceptionPropertyKey.INCORRECT_LOGIN)
    private String login;
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_FIELD)
    @Pattern(regexp = "[A-ZА-Я][а-яa-z]{1,19}", message = ExceptionPropertyKey.INCORRECT_FIELD)
    private String firstName;
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_FIELD)
    @Pattern(regexp = "[A-ZА-Я][а-яa-z]{1,19}", message = ExceptionPropertyKey.INCORRECT_FIELD)
    private String lastName;
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_PASSWORD)
    @Pattern(regexp = "[\\S]{4,16}", message = ExceptionPropertyKey.INCORRECT_PASSWORD)
    private String password;
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_PASSWORD)
    @Pattern(regexp = "[\\S]{4,16}", message = ExceptionPropertyKey.INCORRECT_PASSWORD)
    private String repeatedPassword;

    public RegistrationUserDto() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}