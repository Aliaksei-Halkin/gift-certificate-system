package com.epam.esm.service;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order makeOrder(Long userId, List<Long> certificates);

    List<Order> findUserOrders(Long userId);
}
