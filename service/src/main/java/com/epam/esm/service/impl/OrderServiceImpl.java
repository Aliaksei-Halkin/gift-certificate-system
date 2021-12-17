package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.OrderValidator;
import com.epam.esm.validator.QueryParameterValidator;
import com.epam.esm.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String REGEX_PAGE_KEY = "page";
    private static final String REGEX_PER_PAGE_KEY = "per_page";
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
    public OrderEntity makeOrder(Long userId, List<Long> certificatesIds) {
        List<GiftCertificateEntity> giftCertificates = new ArrayList<>();
        UserValidator.isValidId(userId);
        certificatesIds.forEach((id) -> {
            giftCertificateValidator.isValidId(id);
            GiftCertificateEntity giftCertificate = giftCertificateDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.GIFT_CERTIFICATE_WITH_ID_NOT_FOUND,
                            id, IdentifierEntity.CERTIFICATE));
            giftCertificates.add(giftCertificate);
        });
        BigDecimal cost = giftCertificates.stream().map(GiftCertificateEntity::getPrice).reduce(BigDecimal::add).get();
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.USER_WITH_ID_NOT_FOUND, userId, IdentifierEntity.USER));
        OrderEntity order = new OrderEntity();
        order.setActive(true);
        order.setGiftCertificates(giftCertificates);
        order.setTotalCost(cost);
        order.setUser(user);
        long id = orderDao.add(order);
        order.setOrderId(id);
        return order;
    }

    @Override
    public List<OrderEntity> findUserOrders(Long userId) {
        UserValidator.isValidId(userId);
        return orderDao.findUserOrders(userId);
    }

    @Override
    public List<TagEntity> findMostWidelyUsedTag() {
        UserEntity user = orderDao.findTopUser();
        List<Object[]> listOfTagsAndQuantity = orderDao.findPopularTag(user.getUserId());
        Map<TagEntity, Long> resultMap = extractTagsAndValues(listOfTagsAndQuantity);
        return getPopularTagFromSortedMap(resultMap);
    }

    private List<TagEntity> getPopularTagFromSortedMap(Map<TagEntity, Long> sortedResultMap) {
        List<TagEntity> tags = new ArrayList<>();
        Long maxQuantity = -1L;
        for (Map.Entry<TagEntity, Long> entry : sortedResultMap.entrySet()) {
            if (tags.isEmpty()) {
                tags.add(entry.getKey());
                maxQuantity = entry.getValue();
                continue;
            }
            if (entry.getValue().equals(maxQuantity)) {
                tags.add(entry.getKey());
            } else {
                break;
            }
        }
        return tags;
    }

    private Map<TagEntity, Long> extractTagsAndValues(List<Object[]> listOfTagsAndQuantity) {
        Map<TagEntity, Long> resultMap = new LinkedHashMap<>();
        listOfTagsAndQuantity.stream().forEach(result -> {
            TagEntity tag1 = (TagEntity) result[0];
            Long quantity = (Long) result[1];
            resultMap.put(tag1, quantity);
        });
        return resultMap;
    }

    @Override
    public OrderEntity findOrderById(long orderId) {
        OrderValidator.isValidId(orderId);
        Optional<OrderEntity> order = orderDao.findById(orderId);
        return order.orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.ORDER_WITH_ID_NOT_FOUND,
                orderId, IdentifierEntity.ORDER));
    }

    @Override
    public List<OrderEntity> findAll(Map<String, String> pages) {
        String page = pages.get(REGEX_PAGE_KEY);
        String perPage = pages.get(REGEX_PER_PAGE_KEY);
        QueryParameterValidator.isValidPage(page);
        QueryParameterValidator.isValidPage(perPage);
        int firstPage = Integer.parseInt(page);
        int numberOfRowOnPage = Integer.parseInt(perPage);
        countTotalPages(firstPage, numberOfRowOnPage);
        List<OrderEntity> orders = orderDao.findAll(firstPage, numberOfRowOnPage);
        return orders;
    }

    private void countTotalPages(int page, int perPage) {
        long totalNumbersOfRows = orderDao.countTotalRows(page, perPage);
        long counterPages;
        if (totalNumbersOfRows % perPage == 0) {
            counterPages = totalNumbersOfRows / perPage;
        } else {
            counterPages = totalNumbersOfRows / perPage + 1;
        }
        if (page > counterPages) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.INCORRECT_MAX_PAGE, counterPages,
                    IdentifierEntity.ORDER);
        }
    }
}
