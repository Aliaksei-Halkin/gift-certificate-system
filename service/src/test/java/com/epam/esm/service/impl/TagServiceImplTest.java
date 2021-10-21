package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
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
    private TagService tagService = new TagServiceImpl(tagDao, new TagValidator());
    Tag tag = new Tag();

    @BeforeEach
    void beforeAll() {
        tag.setId(1L);
        tag.setName("Hi");
    }

    @Test
    void when_AddTag_ThenShouldReturnTagDto() {
        Tag tagActual = new Tag();
        tagActual.setId(2L);
        tagActual.setName("Hi");
        when(tagDao.add(tagActual)).thenReturn(1L);
        Tag mockedTag = tagService.addTag(tagActual);
        verify(tagDao).add(any(Tag.class));
        assertEquals(tag, mockedTag);
    }

    @Test
    void when_AddTag_ThenShouldThrowException() {
        Tag tag = new Tag();
        tag.setName("<test*>");
        assertThrows(ValidationException.class, () -> tagService.addTag(tag));
    }

    @Test
    void when_FindAllTags_ThenShouldReturnSetTags() {
        when(tagDao.findAll()).thenReturn(Collections.singletonList(tag));
        Set<Tag> allTags = tagService.findAllTags();
        verify(tagDao).findAll();
        assertEquals(1, allTags.size());
    }

    @Test
    void when_FindTagById_ThenShouldReturnTagDto() {
        when(tagDao.findById(tag.getId())).thenReturn(Optional.of(tag));
        Tag mockedTag = tagService.findTagById(tag.getId());
        verify(tagDao).findById(anyLong());
        assertEquals(tag, mockedTag);
    }

    @Test
    void when_FindTagById_ThenShouldThrowException() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tagService.findTagById(123));
        verify(tagDao).findById(anyLong());
    }

    @Test
    void when_DeleteTagById_ThenShouldNotThrowException() {
        doNothing().when(tagDao).removeById(tag.getId());
        assertDoesNotThrow(() -> tagService.deleteTagById(tag.getId()));
    }

    @Test
    void whenDeleteTagByIdThenShouldThrowException() {
        Long tagId = -1L;
        doNothing().when(tagDao).removeById(tagId);
    }
}