package com.epam.esm.dto;

import com.epam.esm.entity.OrderEntity;
import com.epam.esm.exception.ExceptionPropertyKey;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

public class UserDto extends RepresentationModel<TagDto> {
    @Min(value = 1, message = ExceptionPropertyKey.INCORRECT_ID)
    @Positive(message = ExceptionPropertyKey.INCORRECT_ID)
    private long userId;
    @NotBlank
    @Pattern(regexp = "[A-ZА-Я][а-яa-z]{1,19}", message = ExceptionPropertyKey.INCORRECT_FIELD)
    private String firstName;
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_FIELD)
    @Pattern(regexp = "[A-ZА-Я][а-яa-z]{1,19}", message = ExceptionPropertyKey.INCORRECT_FIELD)
    private String lastName;
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_LOGIN)
    @Pattern(regexp = "\\w{1,20}", message = ExceptionPropertyKey.INCORRECT_LOGIN)
    private String login;
    private String email;
    private List<OrderEntity> orders;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return userId == userDto.userId && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(email, userDto.email) && Objects.equals(orders, userDto.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, email, orders);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", orders=" + orders +
                '}';
    }
}
