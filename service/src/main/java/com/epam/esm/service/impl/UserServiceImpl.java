package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.QueryParameterValidator;
import com.epam.esm.validator.UserValadator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    private static final int INITIAL_PAGE_VALUE = 1;
    private static final String PAGE = "page";
    private static final String PER_PAGE = "per_page";
    private final UserDao userDao;
    private final OrderDao orderDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    @Override
    public List<User> findAll(Map<String, String> queryParameters) {
        QueryParameterValidator.isValidPage(queryParameters.get(PAGE));
        QueryParameterValidator.isValidPage(queryParameters.get(PER_PAGE));
        countTotalPages(queryParameters);
        List<User> users = userDao.findAll(queryParameters);
        return users;
    }

    @Override
    public User findById(long id) {
        UserValadator.isValidId(id);
        User user = checkAndGetUser(id);
        return user;
    }


    private User checkAndGetUser(long id) {
        Optional<User> userOptional = userDao.findById(id);
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
