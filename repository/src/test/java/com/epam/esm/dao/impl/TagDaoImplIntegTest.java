package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliaksei Halkin
 */
class TagDaoImplIntegTest {
    private EmbeddedDatabase database;
    private TagDao tagDao;

    @BeforeEach
    void setData() {
        database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql").addScript("classpath:test-data.sql").build();
        tagDao = new TagDaoImpl(new JdbcTemplate(database), new TagMapper());
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
        tagDao = null;
    }

    @Test
    void whenFindByExistIdThenShouldReturnTrue() {
        Optional<Tag> tag = tagDao.findById(1L);
        assertTrue(tag.isPresent());
    }
    @Test
    void whenFindByNotExistIdThenShouldReturnFalse() {
        Optional<Tag> tag = tagDao.findById(100L);
        assertFalse(tag.isPresent());
    }
    @Test
    void whenFindAllThenShouldReturnListTags() {
        List<Tag> tagList = tagDao.findAll();
        assertEquals(8, tagList.size());
    }
    @Test
    void whenAddCorrectTagThenShouldReturnCorrectTag() {
        Tag tag = new Tag();
        tag.setName("Drive");
        long actual = tagDao.add(tag);
        assertEquals(9L, actual);
    }
    @Test
    void whenAddIncorrectTagThenShouldThrowException() {
        Tag tag = new Tag();
        tag.setName(null);
        assertThrows(DataIntegrityViolationException.class, () -> tagDao.add(tag));
    }
    @Test
    void whenRemoveByIdThenShouldListTagsLessOne() {
        List<Tag> tagList = tagDao.findAll();
        int expected = tagList.size();
        tagDao.removeById(1L);
        List<Tag> tagListAfterRemove = tagDao.findAll();
        int actual = tagListAfterRemove.size();
        assertNotEquals(expected, actual);
    }
    @Test
    void whenFindTagByExistNameThenShouldReturnTrue() {
        Optional<Tag> optionalTag = tagDao.findTagByName("rest");
        boolean condition = optionalTag.isPresent();
        assertTrue(condition);
    }
    @Test
    void whenFindTagByNotExistNameThenShouldReturnFalse() {
        Optional<Tag> optionalTag = tagDao.findTagByName("game");
        boolean condition = optionalTag.isPresent();
        assertFalse(condition);
    }



}