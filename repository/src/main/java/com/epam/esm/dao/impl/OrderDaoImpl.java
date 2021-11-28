package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    public static final String SELECT_USER_ORDERS = "SELECT o FROM Order o WHERE  o.user.userId =?1";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> findById(Long id) {
        throw new UnsupportedOperationException("NO ACTION");
    }

    @Override
    public long add(Order entity) {
        entityManager.persist(entity);
        return entity.getOrderId();
    }

    @Override
    public Order update(Order entity) {
        throw new UnsupportedOperationException("NO ACTION");
    }

    @Override
    public List<Order> findUserOrders(Long userId) {
        return entityManager.createQuery(SELECT_USER_ORDERS, Order.class)
                .setParameter(1, userId).getResultList();
    }
}
