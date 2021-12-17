package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderDto makeOrder(Long userId, List<Long> certificates);

    List<OrderDto> findUserOrders(Long userId);

    List<TagDto> findMostWidelyUsedTag();

    OrderDto findOrderById(long orderId);

    List<OrderDto> findAll(Map<String, String> pagingParameters);
}
