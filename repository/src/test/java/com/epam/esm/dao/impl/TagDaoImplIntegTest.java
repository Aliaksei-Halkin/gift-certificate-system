package com.epam.esm.dao.impl;

import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliaksei Halkin
 */
@SpringBootTest(classes = {RepositoryTestConfig.class})
@ActiveProfiles("test")
@Transactional
class TagDaoImplIntegTest {

    @Autowired
    private TagDao tagDao;

    @Test
    void whenFindByExistIdThenShouldReturnTrue() {
        Optional<TagEntity> tag = tagDao.findById(1L);
        assertTrue(tag.isPresent());
    }

    @Test
    void whenFindByNotExistIdThenShouldReturnFalse() {
        Optional<TagEntity> tag = tagDao.findById(100L);
        assertFalse(tag.isPresent());
    }

    @Test
    void whenFindAllThenShouldReturnListTags() {
        List<TagEntity> tagList = tagDao.findAll(1,100);
        assertEquals(8, tagList.size());
    }

    @Test
    void whenAddCorrectTagThenShouldReturnCorrectTag() {
        TagEntity tag = new TagEntity();
        tag.setName("Drive");
        long actual = tagDao.add(tag);
        assertEquals(9L, actual);
    }

    @Test
    void whenAddIncorrectTagThenShouldThrowException() {
        TagEntity tag = new TagEntity();
        tag.setName(null);
        assertThrows(PersistenceException.class, () -> tagDao.add(tag));
    }

    @Test
    void whenRemoveByIdThenShouldListTagsLessOne() {
        List<TagEntity> tagList = tagDao.findAll(1,100);
        int expected = tagList.size();
        tagDao.removeById(1L);
        List<TagEntity> tagListAfterRemove = tagDao.findAll(1,100);
        int actual = tagListAfterRemove.size();
        assertNotEquals(expected, actual);
    }

    @Test
    void whenFindTagByExistNameThenShouldReturnTrue() {
        Optional<TagEntity> optionalTag = tagDao.findByName("rest");
        boolean condition = optionalTag.isPresent();
        assertTrue(condition);
    }

    @Test
    void whenFindTagByNotExistNameThenShouldReturnFalse() {
        Optional<TagEntity> optionalTag = tagDao.findByName("game");
        boolean condition = optionalTag.isPresent();
        assertFalse(condition);
    }
}