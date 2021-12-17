package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private static final String PAGE = "page";
    private static final String PER_PAGE = "per_page";
    private static final String SELECT_ALL_USERS = "FROM User";
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public long add(UserEntity entity) {
        entityManager.persist(entity);
        return entity.getUserId();
    }

    @Override
    public UserEntity update(UserEntity entity) {
        return null;
    }

    @Override
    public List<UserEntity> findAll(Map<String, String> queryParameters) {
        int page = Integer.parseInt(queryParameters.get(PAGE));
        int perPage = Integer.parseInt(queryParameters.get(PER_PAGE));
        int firstResult = page == 1 ? 0 : page * perPage - perPage;
        return entityManager.createQuery(SELECT_ALL_USERS, UserEntity.class)
                .setFirstResult(firstResult)
                .setMaxResults(perPage)
                .getResultList();
    }

    @Override
    public long countTotalRows(Map<String, String> localQueryParameters) {
        return entityManager.createQuery(SELECT_ALL_USERS).getResultStream().count();
    }
}
