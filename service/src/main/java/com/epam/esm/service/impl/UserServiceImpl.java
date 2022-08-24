package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.RegistrationUserDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.OrderValidator;
import com.epam.esm.validator.QueryParameterValidator;
import com.epam.esm.validator.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    private static final int INITIAL_PAGE_VALUE = 1;
    private static final String PAGE = "page";
    private static final String PER_PAGE = "per_page";
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, OrderDao orderDao, ModelMapper modelMapper) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDto> findAll(Map<String, String> queryParameters) {
        QueryParameterValidator.isValidPage(queryParameters.get(PAGE));
        QueryParameterValidator.isValidPage(queryParameters.get(PER_PAGE));
        countTotalPages(queryParameters);
        List<UserEntity> users = userDao.findAll(queryParameters);
        return users
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        UserValidator.isValidId(id);
        UserEntity user = checkAndGetUser(id);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public OrderDto findUserOrder(Long userId, Long orderId) {
        UserValidator.isValidId(userId);
        OrderValidator.isValidId(orderId);
        Optional<OrderEntity> orderOptional = orderDao.findUserOrder(userId, orderId);
        OrderEntity order = orderOptional.orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.USER_WITH_ID_NOT_FOUND, userId,
                IdentifierEntity.USER));
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        return orderDto;
    }

    @Override
    public UserDto add(RegistrationUserDto userDto) {
        return null;
    }

    private UserEntity checkAndGetUser(long id) {
        Optional<UserEntity> userOptional = userDao.findById(id);
        return userOptional
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.USER_WITH_ID_NOT_FOUND, id,
                        IdentifierEntity.USER));
    }

    private void countTotalPages(Map<String, String> queryParameters) {
        Map<String, String> localQueryParameters = new HashMap<>(queryParameters);
        int page = Integer.parseInt(localQueryParameters.get(PAGE));
        int perPage = Integer.parseInt(localQueryParameters.get(PER_PAGE));
        long totalNumbersOfRows = userDao.countTotalRows(localQueryParameters);
        long counterPages = INITIAL_PAGE_VALUE;
        if (totalNumbersOfRows % perPage == 0) {
            counterPages = totalNumbersOfRows / perPage;
        } else {
            counterPages = totalNumbersOfRows / perPage + 1;
        }
        if (page > counterPages) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.INCORRECT_MAX_PAGE, counterPages,
                    IdentifierEntity.USER);
        }
    }
}
