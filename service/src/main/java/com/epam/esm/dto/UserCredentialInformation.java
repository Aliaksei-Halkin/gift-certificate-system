package com.epam.esm.dto;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class UserCredentialInformation implements UserDetails {
    private final long userId;
    private final String login;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isAdmin;

    public UserCredentialInformation(long userId, String login, String password, List<SimpleGrantedAuthority> authorities, boolean isAdmin) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.authorities = authorities;
        this.isAdmin = isAdmin;
    }

    public String getLogin() {
        return login;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}