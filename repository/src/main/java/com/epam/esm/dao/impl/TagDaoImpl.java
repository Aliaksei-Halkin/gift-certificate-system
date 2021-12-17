package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.TagEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * The class represents Tag dao implementation.
 * This class use {@link JdbcTemplate} to do standard CRUD operations in a database with table tag.
 *
 * @author Aliaksei Halkin
 */
@Repository
public class TagDaoImpl implements TagDao {
    /**
     * This is the query SELECT to database
     */
    public static final String SELECT_ALL_TAGS = "  FROM Tag  ";
    /**
     * This is the query SELECT to database
     */
    public static final String SELECT_TAG_BY_NAME = "  FROM Tag WHERE name = :name";
    private static final String NAME_PARAMETER = "name";
    /**
     * This is the query DELETE to database, the active value of Tag set false
     */
    public static final String DELETE_TAG_BY_ID = "UPDATE TagEntity SET active=FALSE WHERE id = :id ";
    private static final String ID_PARAMETER = "id";
    public static final String ACTIVATE_TAG_BY_NAME = "UPDATE tags SET active=true WHERE tagName = :name";

    @PersistenceContext
    private EntityManager entityManager;


    /**
     *
     * The method find   Tag by id
     *
     * @param id {@code Long} the id of the Tag
     * @return Optional with {@link TagEntity} entity
     */
    @Override
    public Optional<TagEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TagEntity.class, id));
    }

    /**
     * The method  add tag to database
     *
     * @param entity The entity for adding
     * @return id  of Tag
     */
    @Override
    public long add(TagEntity entity) {
        entityManager.persist(entity);
        return entity.getId();
    }

    /**
     * The method delete tag by id
     *
     * @param id of certificate
     */
    @Override
    public void removeById(Long id) {
        entityManager.createQuery(DELETE_TAG_BY_ID)
                .setParameter(ID_PARAMETER, id).executeUpdate();
    }

    /**
     * The method update tag by id
     *
     * @param entity Tag for update
     * @return updating Tag
     */

    @Override
    public TagEntity update(TagEntity entity) {
        throw new UnsupportedOperationException("Update is not available action for Tag");
    }

      /**
     * The method find all tags
     *
     * @return {@code List} of all tags
     */
    @Override
    public List<TagEntity> findAll(int page, int perPage) {
        int firstResult = page == 1 ? 0 : page * perPage - perPage;
        return entityManager.createQuery(SELECT_ALL_TAGS, TagEntity.class)
                .setFirstResult(firstResult).setMaxResults(perPage).getResultList();
    }

    @Override
    public long countTotalRows(int page, int perPage) {
        return entityManager.createQuery(SELECT_ALL_TAGS, TagEntity.class)
               .getResultStream().count();
    }

    /**
     * The method find tag y id
     * *
     *
     * @return {@code Optional<Tag>} tag
     */
    @Override
    public Optional<TagEntity> findByName(String name) {
        return entityManager.createQuery(SELECT_TAG_BY_NAME, TagEntity.class)
                .setParameter(NAME_PARAMETER, name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void changeActiveForTag(String name) {
        entityManager.createNativeQuery(ACTIVATE_TAG_BY_NAME, TagEntity.class)
                .setParameter(NAME_PARAMETER, name)
                .executeUpdate();
    }
}
