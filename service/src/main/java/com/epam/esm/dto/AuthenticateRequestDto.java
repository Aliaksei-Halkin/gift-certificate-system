package com.epam.esm.dto;

import com.epam.esm.exception.ExceptionPropertyKey;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class AuthenticateRequestDto {
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_LOGIN)
    @Pattern(regexp = "\\w{1,20}", message = ExceptionPropertyKey.INCORRECT_LOGIN)
    private String login;
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_PASSWORD)
    @Pattern(regexp = "[\\S]{4,16}", message = ExceptionPropertyKey.INCORRECT_PASSWORD)
    private String password;

    public AuthenticateRequestDto() {
    }

    public AuthenticateRequestDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}