package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
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
    public static final String SELECT_ALL_TAGS = "  FROM Tag WHERE  active = true";
    /**
     * This is the query SELECT to database
     */
    public static final String SELECT_TAG_BY_NAME = "  FROM Tag WHERE name = :name";
    private static final String NAME_PARAMETER = "name";
    /**
     * This is the query INSERT to database
     */
    public static final String INSERT_TAG = "INSERT INTO tags (tagName) VALUES (?)";
    /**
     * This is the query DELETE to database, the active value of Tag set false
     */
    public static final String DELETE_TAG_BY_ID = "UPDATE Tag SET active=false WHERE id = :id";
    private static final String ID_PARAMETER = "id";
    public static final String RETURN_DELETED_TAG = "UPDATE Tag SET active=true WHERE name = :name";

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * The method find   Tag by id
     *
     * @param id {@code Long} the id of the Tag
     * @return Optional with {@link Tag} entity
     */
    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    /**
     * The method  add tag to database
     *
     * @param entity The entity for adding
     * @return id  of Tag
     */
    @Override
    public long add(Tag entity) {
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
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update is not available action for Tag");
    }

    /**
     * The method find all tags
     *
     * @return {@code List} of all tags
     */
    @Override
    public List<Tag> findAll() {
        return entityManager.createQuery(SELECT_ALL_TAGS, Tag.class).getResultList();
    }

    /**
     * The method find tag y id
     * *
     *
     * @return {@code Optional<Tag>} tag
     */
    @Override
    public Optional<Tag> findTagByName(String name) {
        return entityManager.createQuery(SELECT_TAG_BY_NAME, Tag.class)
                .setParameter(NAME_PARAMETER, name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void changeActiveForTag(String name) {
        entityManager.createQuery(RETURN_DELETED_TAG, Tag.class)
                .setParameter(NAME_PARAMETER, name)
                .executeUpdate();
    }
}
