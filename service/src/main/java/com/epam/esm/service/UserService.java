package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> findAll(Map<String, String> queryParameters);

    User findById(long id);

    OrderDto findUserOrder(Long userId, Long orderId);
}
