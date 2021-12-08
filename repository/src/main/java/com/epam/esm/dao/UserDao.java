package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Map;

public interface UserDao extends BaseDao<User, Long> {

    List<User> findAll(Map<String, String> queryParam);

    long countTotalRows(Map<String, String> localQueryParameters);
}
