package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends BaseDao<Order, Long> {

    List<Order> findUserOrders(Long userId);

    Optional<Order> findUserOrder(Long userId, Long orderId);
}
