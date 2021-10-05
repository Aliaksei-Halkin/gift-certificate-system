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
 * @author Aliaksei Halkin
 */
@Repository
public class TagDaoImpl implements TagDao {
    public static final String SELECT_ALL_TAGS = "SELECT tagId, tagName FROM tags";
    public static final String SELECT_TAG_BY_NAME = "SELECT tagId, tagName FROM tags WHERE tagName = ?";
    public static final String SELECT_TAG_BY_ID = "SELECT tagId, tagName FROM tags WHERE tagId = ?";
    public static final String INSERT_TAG = "INSERT INTO tags (tagName) VALUES (?)";
    public static final String DELETE_TAG = "DELETE FROM tags WHERE tagId = ?";
    private final JdbcTemplate jdbcTemplate;
    private final TagMapper tagMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, TagMapper tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagMapper = tagMapper;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return jdbcTemplate.query(SELECT_TAG_BY_ID, tagMapper, id).stream().findFirst();
    }


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

    @Override
    public void removeById(Long id) {
        jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update is not available action for Tag");
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL_TAGS, tagMapper);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return jdbcTemplate.query(SELECT_TAG_BY_NAME, tagMapper, name).stream().findFirst();
    }
}
