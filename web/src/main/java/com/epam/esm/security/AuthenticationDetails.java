package com.epam.esm.security;

import java.util.Objects;

public class AuthenticationDetails {
    private long id;
    private boolean isAdmin;

    public AuthenticationDetails() {
    }

    public AuthenticationDetails(long id, boolean isAdmin) {
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationDetails that = (AuthenticationDetails) o;
        return id == that.id && isAdmin == that.isAdmin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isAdmin);
    }
}