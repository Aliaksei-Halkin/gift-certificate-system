package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    public static final String SELECT_USER_ORDERS = "SELECT o FROM OrderEntity o WHERE  o.user.userId =?1";
    private static final String SELECT_USER_ORDER = "SELECT o FROM OrderEntity o WHERE  o.user.userId =?1 " +
            " AND o.orderId =?2 ";
    private static final String SELECT_TOP_USER =
            "SELECT o.user FROM OrderEntity o GROUP BY o.user.userId ORDER BY SUM(o.totalCost) DESC ";
    private static final String SELECT_TOP_TAG =
            "SELECT t, count(t.id) AS quantity FROM OrderEntity o " +
                    " JOIN o.giftCertificates g " +
                    " JOIN g.tags t " +
                    " JOIN o.user u WHERE u.userId =?1 " +
                    " GROUP BY t.id " +
                    " ORDER BY count(t.id) DESC ";
    private static final String SELECT_ALL_ORDERS = "SELECT o FROM OrderEntity o";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<OrderEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(OrderEntity.class, id));
    }

    @Override
    public long add(OrderEntity entity) {
        entityManager.persist(entity);
        return entity.getOrderId();
    }

    @Override
    public OrderEntity update(OrderEntity entity) {
        throw new UnsupportedOperationException("NO ACTION");
    }

    @Override
    public List<OrderEntity> findUserOrders(Long userId) {
        return entityManager.createQuery(SELECT_USER_ORDERS, OrderEntity.class)
                .setParameter(1, userId).getResultList();
    }

    @Override
    public Optional<OrderEntity> findUserOrder(Long userId, Long orderId) {
        return entityManager.createQuery(SELECT_USER_ORDER, OrderEntity.class)
                .setParameter(1, userId)
                .setParameter(2, orderId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public UserEntity findTopUser() {
        return entityManager.createQuery(SELECT_TOP_USER, UserEntity.class)
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
    public List<OrderEntity> findAll(int page, int perPage) {
        int firstResult = page == 1 ? 0 : page * perPage - perPage;
        return entityManager.createQuery(SELECT_ALL_ORDERS, OrderEntity.class)
                .setFirstResult(firstResult).setMaxResults(perPage).getResultList();
    }

    @Override
    public long countTotalRows(int page, int perPage) {
        return entityManager.createQuery(SELECT_ALL_ORDERS, OrderEntity.class).getResultStream().count();
    }
}
