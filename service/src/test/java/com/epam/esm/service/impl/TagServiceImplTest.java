package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Aliaksei Halkin
 */
class TagServiceImplTest {
    private TagDao tagDao = mock(TagDaoImpl.class);
    private TagService tagService = new TagServiceImpl(tagDao);

    @Test
    void whenAddTagThenShouldReturnTagDto() {
        Tag tagExpected = new Tag();
        tagExpected.setName("Hi");
        tagExpected.setId(1L);
        Tag tagActual = new Tag();
        tagActual.setId(2L);
        tagActual.setName("Hi");
        when(tagDao.add(tagActual)).thenReturn(1L);
        Tag mockedTag = tagService.addTag(tagActual);
        assertEquals(tagExpected, mockedTag);
    }

    @Test
    void whenAddTagThenShouldThrowException() {
        Tag tag = new Tag();
        tag.setName("<test*>");
        assertThrows(ValidationException.class, () -> tagService.addTag(tag));
    }

    @Test
    void whenFindAllTagsThenShouldReturnSetTags() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("testTag");

        when(tagDao.findAll()).thenReturn(Collections.singletonList(tag));
        Set<Tag> allTags = tagService.findAllTags();
        assertEquals(1, allTags.size());
    }

    @Test
    void whenFindTagByIdThenShouldReturnTagDto() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("testTag");

        when(tagDao.findById(tag.getId())).thenReturn(Optional.of(tag));
        Tag mockedTag = tagService.findTagById(tag.getId());
        assertEquals(tag, mockedTag);
    }

    @Test
    void whenFindTagByIdThenShouldThrowException() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tagService.findTagById(123));
    }

    @Test
    void whenDeleteTagByIdThenShouldNotThrowException() {
        long tagId = 1;
        doNothing().when(tagDao).removeById(tagId);

        assertDoesNotThrow(() -> tagService.deleteTagById(tagId));
    }

    @Test
    void whenDeleteTagByIdThenShouldThrowException() {
        long tagId = -1;
        doNothing().when(tagDao).removeById(tagId);

        assertThrows(ValidationException.class, () -> tagService.deleteTagById(tagId));
    }
}