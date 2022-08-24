package com.epam.esm.dto;




public class AuthenticateResponseDto {
    private long id;
    private String login;
    private String token;

    public AuthenticateResponseDto() {
    }

    public AuthenticateResponseDto(long id, String login, String token) {
        this.id = id;
        this.login = login;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}