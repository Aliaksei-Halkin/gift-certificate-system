package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserEntity> findAll(Map<String, String> queryParameters);

    UserEntity findById(long id);

    OrderDto findUserOrder(Long userId, Long orderId);
}
