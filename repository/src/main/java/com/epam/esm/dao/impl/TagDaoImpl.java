package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
    public static final String SELECT_ALL_TAGS = "SELECT tagId, tagName FROM tags";
    /**
     * This is the query SELECT to database
     */
    public static final String SELECT_TAG_BY_NAME = "SELECT tagId, tagName FROM tags WHERE tagName = ?";
    /**
     * This is the query SELECT to database
     */
    public static final String SELECT_TAG_BY_ID = "SELECT tagId, tagName FROM tags WHERE tagId = ?";
    /**
     * This is the query INSERT to database
     */
    public static final String INSERT_TAG = "INSERT INTO tags (tagName) VALUES (?)";
    /**
     * This is the query DELETE to database
     */
    public static final String DELETE_TAG = "DELETE FROM tags WHERE tagId = ?";
    /**
     * The {@link JdbcTemplate} object
     */
    private final JdbcTemplate jdbcTemplate;
    /**
     * The {@link TagMapper} object
     */
    private final TagMapper tagMapper;

    /**
     * The constructor with all parameters, used to create instance of {@code TagDaoImpl}
     *
     * @param jdbcTemplate The {@link JdbcTemplate} object
     * @param tagMapper    The {@link TagMapper} object
     */
    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, TagMapper tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagMapper = tagMapper;
    }

    /**
     * The method find   Tag by id
     *
     * @param id {@code Long} the id of the Tag
     * @return Optional with {@link Tag} entity
     */
    @Override
    public Optional<Tag> findById(Long id) {
        return jdbcTemplate.query(SELECT_TAG_BY_ID, tagMapper, id).stream().findFirst();
    }

    /**
     * The method  add tag to database
     *
     * @param entity The entity for adding
     * @return id  of Tag
     */
    @Override
    public long add(Tag entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT_TAG, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            return preparedStatement;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().longValue();
        }
        throw new RuntimeException("Generated tagId not found");
    }

    /**
     * The method delete tag by id
     *
     * @param id of certificate
     */
    @Override
    public void removeById(Long id) {
        jdbcTemplate.update(DELETE_TAG, id);
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
        return jdbcTemplate.query(SELECT_ALL_TAGS, tagMapper);
    }

    /**
     * The method find tag y id
     * *
     *
     * @return {@code Optional<Tag>} tag
     */
    @Override
    public Optional<Tag> findTagByName(String name) {
        return jdbcTemplate.query(SELECT_TAG_BY_NAME, tagMapper, name).stream().findFirst();
    }
}
