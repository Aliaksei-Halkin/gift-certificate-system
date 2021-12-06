package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.OrderDaoImpl;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
    private GiftCertificateDao giftCertificateDao = mock(GiftCertificateDaoImpl.class);
    private TagDao tagDao = mock(TagDaoImpl.class);
    private OrderDao orderDao = mock(OrderDaoImpl.class);
    private UserDao userDao = mock(UserDao.class);
    private OrderService orderService =
            new OrderServiceImpl(giftCertificateDao, new GiftCertificateValidator(giftCertificateDao),
                    userDao, orderDao, tagDao);
    GiftCertificate giftCertificate = new GiftCertificate();
    User user = new User();
    Order order = new Order();

    @BeforeEach
    void beforeAll() {
        giftCertificate.setId(1L);
        giftCertificate.setName("Hello");
        giftCertificate.setDescription("Hello from description");
        giftCertificate.setPrice(new BigDecimal("123"));
        giftCertificate.setDuration(1);
        user.setUserId(1);
        user.setFirstName("Nick");
        user.setLastName("Smith");
        user.setEmail("post@gmail.com");
        order.setOrderId(1);
        order.setCreateDate(LocalDateTime.now());
        order.setGiftCertificates(Collections.singletonList(giftCertificate));
        order.setTotalCost(giftCertificate.getPrice());
        order.setActive(true);
        order.setUser(user);
    }

    @Test
    void when_MakeOrder_ThenShould_ReturnOrder() {
        when(giftCertificateDao.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(userDao.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(orderDao.add(order)).thenReturn(order.getOrderId());
        Order createdOrder = orderService.makeOrder(user.getUserId(), Collections.singletonList(giftCertificate.getId()));
        createdOrder.setCreateDate(order.getCreateDate());
        createdOrder.setOrderId(1);
        verify(giftCertificateDao).findById(anyLong());
        verify(userDao).findById(anyLong());
        verify(orderDao).add(any(Order.class));
        assertEquals(order, createdOrder);
    }

    @Test
    void when_MakeOrder_ThenShould_ThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(145L);
        when(giftCertificateDao.findById(giftCertificate.getId())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> orderService.makeOrder(1L, Collections.singletonList(145L)));
        verify(giftCertificateDao).findById(anyLong());
    }

    @Test
    void when_FindOrderByExistId_ThenShould_ReturnOrder() {
        when(orderDao.findById(order.getOrderId())).thenReturn(Optional.of(order));
        Order foundOrder = orderService.findOrderById(order.getOrderId());
        verify(orderDao).findById(anyLong());
        assertEquals(order, foundOrder);
    }

    @Test
    void when_FindOrderByNotExistId_ThenShould_ThrowException() {
        Order order = new Order();
        order.setOrderId(3312331);
        when(orderDao.findById(order.getOrderId())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> orderService.findOrderById(order.getOrderId()));
        verify(orderDao).findById(anyLong());
    }

    @Test
    void when_FindMostWidelyUsedTag_WithHighestCostOfAllOrders_ThenShouldReturnTag() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("spring");
        Object[] arrayTagsWithQuantity = new Object[]{tag, 1L};
        when(orderDao.findTopUser()).thenReturn(user);
        when(orderDao.findPopularTag(user.getUserId())).thenReturn(Collections.singletonList(arrayTagsWithQuantity));
        List<Tag> tags = orderService.findMostWidelyUsedTag();
        verify(orderDao).findPopularTag(anyLong());
        verify(orderDao).findTopUser();
        assertEquals(tags.get(0), tag);
    }

    @Test
    void when_FindMostWidelyUsedTagWithHighestCostOfAllOrders_ThenShould_ThrowException() {
        when(orderDao.findTopUser()).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> orderService.findMostWidelyUsedTag());
        verify(orderDao).findTopUser();
    }


    @Test
    void when_FindAllOrder_Then_ShouldReturnOrders() {
        String page = "1";
        String per_page = "10";
        Map<String, String> paging = new HashMap<>();
        paging.put("page", page);
        paging.put("per_page", per_page);
        when(orderDao.findAll(anyInt(), anyInt())).thenReturn(Collections.singletonList(order));
        when(orderDao.countTotalRows(anyInt(), anyInt())).thenReturn(1L);
        List<Order> foundOrder = orderService.findAll(paging);
        verify(orderDao).findAll(anyInt(), anyInt());
        verify(orderDao).countTotalRows(anyInt(), anyInt());
        assertEquals(order, foundOrder.get(0));
    }

    @Test
    void when_FindAllOrder_Then_ShouldThrowException() {
        String page = "10";
        String per_page = "10";
        Map<String, String> paging = new HashMap<>();
        paging.put("page", page);
        paging.put("per_page", per_page);
        when(orderDao.countTotalRows(anyInt(), anyInt())).thenReturn(1L);
        assertThrows(ResourceNotFoundException.class, () -> orderService.findAll(paging));
        verify(orderDao).countTotalRows(anyInt(), anyInt());
    }
}