package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateValidator giftCertificateValidator;
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final TagDao tagDao;

    @Autowired
    public OrderServiceImpl(GiftCertificateDao giftCertificateDao, GiftCertificateValidator giftCertificateValidator,
                            UserDao userDao, OrderDao orderDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.tagDao = tagDao;
    }

    @Transactional
    @Override
    public Order makeOrder(Long userId, List<Long> certificatesIds) {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        UserValidator.isValidId(userId);
        certificatesIds.forEach((id) -> {
            giftCertificateValidator.isValidId(id);
            GiftCertificate giftCertificate = giftCertificateDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.GIFT_CERTIFICATE_WITH_ID_NOT_FOUND,
                            id, IdentifierEntity.CERTIFICATE));
            giftCertificates.add(giftCertificate);
        });
        BigDecimal cost = giftCertificates.stream().map(GiftCertificate::getPrice).reduce(BigDecimal::add).get();
        User user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.USER_WITH_ID_NOT_FOUND, userId, IdentifierEntity.USER));
        Order order = new Order();
        order.setActive(true);
        order.setGiftCertificates(giftCertificates);
        order.setTotalCost(cost);
        order.setUser(user);
        long id = orderDao.add(order);
        order.setOrderId(id);
        return order;
    }

    @Override
    public List<Order> findUserOrders(Long userId) {
        UserValidator.isValidId(userId);
        return orderDao.findUserOrders(userId);
    }

    @Override
    public Tag findMostWidelyUsedTag() {
        User user = orderDao.findTopUser();
        Tag topTag = orderDao.findPopularTag(user.getUserId());
        return topTag;
    }
}
