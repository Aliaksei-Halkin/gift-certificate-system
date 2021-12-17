package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Aliaksei Halkin
 */
class TagServiceImplTest {
    private TagDao tagDao = mock(TagDaoImpl.class);
    private TagService tagService = new TagServiceImpl(tagDao, new TagValidator());
    TagEntity tag = new TagEntity();

    @BeforeEach
    void beforeAll() {
        tag.setId(1L);
        tag.setName("Hi");
        tag.setActive(true);
    }

    @Test
    void when_AddTag_ThenShouldReturnTagDto() {
        TagEntity tagActual = new TagEntity();
        tagActual.setId(2L);
        tagActual.setName("Hi");
        tagActual.setActive(true);
        when(tagDao.add(tagActual)).thenReturn(1L);
        TagEntity mockedTag = tagService.addTag(tagActual);
        verify(tagDao).add(any(TagEntity.class));
        assertEquals(tag, mockedTag);
    }

    @Test
    void when_AddTag_ThenShouldThrowException() {
        TagEntity tag = new TagEntity();
        tag.setName("<test*>");
        assertThrows(ValidationException.class, () -> tagService.addTag(tag));
    }

    @Test
    void when_FindAllTags_ThenShouldReturnSetTags() {
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("page", "1");
        queryParam.put("per_page", "1");
        when(tagDao.findAll(1, 1)).thenReturn(Collections.singletonList(tag));
        when(tagDao.countTotalRows(1, 1)).thenReturn(1L);
        Set<TagEntity> allTags = tagService.findAllTags(queryParam);
        verify(tagDao).findAll(1, 1);
        verify(tagDao).countTotalRows(1, 1);
        assertEquals(1, allTags.size());
    }

    @Test
    void when_FindTagById_ThenShouldReturnTagDto() {
        when(tagDao.findById(tag.getId())).thenReturn(Optional.of(tag));
        TagEntity mockedTag = tagService.findTagById(tag.getId());
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
        when(tagDao.findById(anyLong())).thenReturn(Optional.of(tag));
        doNothing().when(tagDao).removeById(tag.getId());
        assertDoesNotThrow(() -> tagService.deleteTagById(tag.getId()));
        verify(tagDao).findById(anyLong());
    }

    @Test
    void whenDeleteTagByIdThenShouldThrowException() {
        Long tagId = -1L;
        doNothing().when(tagDao).removeById(tagId);
    }
}