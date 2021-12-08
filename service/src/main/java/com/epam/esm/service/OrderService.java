package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order makeOrder(Long userId, List<Long> certificates);

    List<Order> findUserOrders(Long userId);

    List<Tag> findMostWidelyUsedTag();

    Order findOrderById(long orderId);

    List<Order> findAll(Map<String, String> pagingParameters);
}
