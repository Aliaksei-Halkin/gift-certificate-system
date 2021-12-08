package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    public static final String SELECT_USER_ORDERS = "SELECT o FROM Order o WHERE  o.user.userId =?1";
    private static final String SELECT_USER_ORDER = "SELECT o FROM Order o WHERE  o.user.userId =?1 " +
            " AND o.orderId =?2 ";
    private static final String SELECT_TOP_USER =
            "SELECT o.user FROM Order o GROUP BY o.user.userId ORDER BY SUM(o.totalCost) DESC ";
    private static final String SELECT_TOP_TAG =
            "SELECT t, count(t.id) AS quantity FROM Order o " +
                    " JOIN o.giftCertificates g " +
                    " JOIN g.tags t " +
                    " JOIN o.user u WHERE u.userId =?1 " +
                    " GROUP BY t.id " +
                    " ORDER BY count(t.id) DESC ";
    private static final String SELECT_ALL_ORDERS = "SELECT o FROM Order o";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
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

    @Override
    public Optional<Order> findUserOrder(Long userId, Long orderId) {
        return entityManager.createQuery(SELECT_USER_ORDER, Order.class)
                .setParameter(1, userId)
                .setParameter(2, orderId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public User findTopUser() {
        return entityManager.createQuery(SELECT_TOP_USER, User.class)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    public List<Object[]> findPopularTag(long userId) {
        List<Object[]> tag = entityManager.createQuery(SELECT_TOP_TAG)
                .setParameter(1, userId)
                .getResultList();
        return tag;

    }

    @Override
    public List<Order> findAll(int page, int perPage) {
        int firstResult = page == 1 ? 0 : page * perPage - perPage;
        return entityManager.createQuery(SELECT_ALL_ORDERS, Order.class)
                .setFirstResult(firstResult).setMaxResults(perPage).getResultList();
    }

    @Override
    public long countTotalRows(int page, int perPage) {
        return entityManager.createQuery(SELECT_ALL_ORDERS, Order.class).getResultStream().count();
    }
}
