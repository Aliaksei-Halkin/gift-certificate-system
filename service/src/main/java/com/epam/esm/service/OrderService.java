package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;

import java.util.List;

public interface OrderService {
    Order makeOrder(Long userId, List<Long> certificates);

    List<Order> findUserOrders(Long userId);

    Tag findMostWidelyUsedTag();
}
