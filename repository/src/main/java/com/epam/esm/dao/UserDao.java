package com.epam.esm.dao;

import com.epam.esm.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserDao extends BaseDao<UserEntity, Long> {

    List<UserEntity> findAll(Map<String, String> queryParam);

    long countTotalRows(Map<String, String> localQueryParameters);
}
