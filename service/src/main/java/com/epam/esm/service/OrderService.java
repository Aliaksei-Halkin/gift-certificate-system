package com.epam.esm.service;

import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.TagEntity;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderEntity makeOrder(Long userId, List<Long> certificates);

    List<OrderEntity> findUserOrders(Long userId);

    List<TagEntity> findMostWidelyUsedTag();

    OrderEntity findOrderById(long orderId);

    List<OrderEntity> findAll(Map<String, String> pagingParameters);
}
