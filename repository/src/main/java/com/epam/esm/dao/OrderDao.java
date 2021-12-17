package com.epam.esm.dao;

import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends BaseDao<OrderEntity, Long> {

    List<OrderEntity> findUserOrders(Long userId);

    Optional<OrderEntity> findUserOrder(Long userId, Long orderId);

    UserEntity findTopUser();

    List<Object[]> findPopularTag(long userId);

    List<OrderEntity> findAll(int firstPage, int numberOfRowOnPage);

    long countTotalRows(int page, int perPage);
}
