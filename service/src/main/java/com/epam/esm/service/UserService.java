package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDto> findAll(Map<String, String> queryParameters);

    UserDto findById(long id);

    OrderDto findUserOrder(Long userId, Long orderId);
}
