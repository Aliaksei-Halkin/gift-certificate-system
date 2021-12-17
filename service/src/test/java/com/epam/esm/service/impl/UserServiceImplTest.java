package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.impl.OrderDaoImpl;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    private OrderDao orderDao = mock(OrderDaoImpl.class);
    private UserDao userDao = mock(UserDao.class);
    private ModelMapper modelMapper = new ModelMapper();
    UserEntity user = new UserEntity();
    UserService userService = new UserServiceImpl(userDao, orderDao, modelMapper);

    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    @BeforeEach
    void beforeAll() {
        user.setUserId(1);
        user.setFirstName("Nick");
        user.setLastName("Smith");
        user.setEmail("post@gmail.com");
    }

    @Test
    void when_FindUserByExistId_Then_ShouldReturnUser() {
        when(userDao.findById(user.getUserId())).thenReturn(Optional.of(user));
        UserEntity foundUser = userService.findById(user.getUserId());
        verify(userDao).findById(anyLong());
        assertEquals(user, foundUser);
    }

    @Test
    void when_FindUserByNotExistId_Then_ShouldThrowException() {
        when(userDao.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1233555555));
        verify(userDao).findById(anyLong());
    }

    @Test
    void when_FindAllUsers_Then_ShouldReturnListOfUsers() {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("page", "1");
        queryParameters.put("per_page", "10");
        when(userDao.findAll(queryParameters)).thenReturn(Collections.singletonList(user));
        when(userDao.countTotalRows(queryParameters)).thenReturn(1L);
        List<UserEntity> foundUsers = userService.findAll(queryParameters);
        verify(userDao).findAll(anyMap());
        verify(userDao).countTotalRows(anyMap());
        assertEquals(foundUsers.get(0), user);
    }

    @Test
    void when_FindAllUsers_Then_ShouldThrowException() {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("page", "f2");
        assertThrows(ValidationException.class, () -> userService.findAll(queryParameters));
    }

    @Test
    void when_FindUserOrder_Then_ShouldReturnListOfOrders() {
        OrderEntity order = new OrderEntity();
        order.setOrderId(1);
        when(orderDao.findUserOrder(user.getUserId(), order.getOrderId())).thenReturn(Optional.of(order));
        OrderDto foundOrder = userService.findUserOrder(user.getUserId(), 1L);
        verify(orderDao).findUserOrder(anyLong(), anyLong());
        assertEquals(foundOrder, modelMapper.map(order, OrderDto.class));
    }

    @Test
    void when_FindUserOrder_ThenShouldThrowException() {
        UserEntity user = new UserEntity();
        user.setUserId(-1231);
        assertThrows(ValidationException.class, () -> userService.findUserOrder(user.getUserId(), 1L));
    }

}