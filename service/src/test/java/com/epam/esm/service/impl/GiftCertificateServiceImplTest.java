package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.QueryParameter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Aliaksei Halkin
 */
class GiftCertificateServiceImplTest {
    private GiftCertificateDao giftCertificateDao = mock(GiftCertificateDaoImpl.class);
    private TagDao tagDao = mock(TagDaoImpl.class);
    private GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao);


    @Test
    void whenAddGiftCertificateThenShouldReturnGiftCertificate() {
        GiftCertificate giftCertificateFirst = new GiftCertificate();
        giftCertificateFirst.setName("Hello");
        giftCertificateFirst.setDescription("Hello from description");
        giftCertificateFirst.setPrice(new BigDecimal("123"));
        giftCertificateFirst.setDuration(1);
        giftCertificateFirst.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificateFirst.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));

        Set<Tag> tags = new HashSet<>();
        Tag tagFirst = new Tag();
        tagFirst.setId(1L);
        tagFirst.setName("Hi");
        tags.add(tagFirst);
        giftCertificateFirst.setTags(tags);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Hello");
        giftCertificate.setDescription("Hello from description");
        giftCertificate.setPrice(new BigDecimal("123"));
        giftCertificate.setDuration(1);
        giftCertificate.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        Set<Tag> tagsSecond = new HashSet<>();
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Hi");
        tagsSecond.add(tag);
        giftCertificate.setTags(tagsSecond);

        when(giftCertificateDao.add(any(GiftCertificate.class))).thenReturn(giftCertificate.getId());
        when(tagDao.findAll()).thenReturn(Collections.singletonList(tag));
        when(tagDao.findTagByName(tag.getName())).thenReturn(Optional.of(tag));

        GiftCertificate mockedGiftCertificate = giftCertificateService.addGiftCertificate(giftCertificateFirst);
        giftCertificate.setCreatedDate(mockedGiftCertificate.getCreatedDate());
        giftCertificate.setUpdateDate(mockedGiftCertificate.getUpdateDate());
        assertEquals(giftCertificate, mockedGiftCertificate);
    }

    @Test
    void whenAddGiftCertificateThenShouldThrowException() {
        GiftCertificate giftCertificateDto = new GiftCertificate();
        giftCertificateDto.setId(1L);
        giftCertificateDto.setName("Hello");
        giftCertificateDto.setDescription("Hello from description");
        giftCertificateDto.setPrice(new BigDecimal("123"));
        giftCertificateDto.setDuration(1);
        giftCertificateDto.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificateDto.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        Set<Tag> tagsDto = new HashSet<>();
        Tag tagDto = new Tag();
        tagDto.setId(1L);
        tagDto.setName("Hi");
        tagsDto.add(tagDto);
        giftCertificateDto.setTags(tagsDto);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Hello");
        giftCertificate.setDescription("Hello from description");
        giftCertificate.setPrice(new BigDecimal("123"));
        giftCertificate.setDuration(1);
        giftCertificate.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Hi");
        tags.add(tag);
        giftCertificate.setTags(tags);

        when(giftCertificateDao.add(any(GiftCertificate.class))).thenReturn(giftCertificate.getId());
        when(tagDao.findAll()).thenReturn(Collections.singletonList(tag));
        when(tagDao.findTagByName(tag.getName())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.addGiftCertificate(giftCertificateDto));
    }

    @Test
    void whenAddTagToGiftCertificateThenShouldReturnGiftCertificate() {
        Tag tagDto = new Tag();
        tagDto.setName("Hi");

        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Hi");
        tags.add(tag);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Hello");
        giftCertificate.setDescription("Hello from description");
        giftCertificate.setPrice(new BigDecimal("123"));
        giftCertificate.setDuration(1);
        giftCertificate.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setTags(tags);

        when(giftCertificateDao.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(tagDao.findAll()).thenReturn(new LinkedList<>());
        when(tagDao.add(tag)).thenReturn(tag.getId());
        when(giftCertificateDao.update(giftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateDao.findGiftCertificateTags(giftCertificate.getId())).thenReturn(tags);

        GiftCertificate mockedGiftCertificateDto = giftCertificateService.addTagToGiftCertificate(giftCertificate.getId(), tagDto);

        assertEquals(giftCertificate, mockedGiftCertificateDto);
    }

    @Test
    void whenAddTagToGiftCertificateThenShouldThrowException() {
        Tag tagDto = new Tag();
        tagDto.setId(1L);
        tagDto.setName("Hi");

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Hi");

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Hello");
        giftCertificate.setDescription("Hello from description");
        giftCertificate.setPrice(new BigDecimal("123"));
        giftCertificate.setDuration(1);
        giftCertificate.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));

        when(giftCertificateDao.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(tagDao.findAll()).thenReturn(Collections.singletonList(tag));
        when(tagDao.findTagByName(tag.getName())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.addTagToGiftCertificate(giftCertificate.getId(), tagDto));
    }

    @Test
    void whenFindGiftCertificateByIdThenShouldReturnGiftCertificate() {
        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Hi");
        tags.add(tag);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Hello");
        giftCertificate.setDescription("Hello from description");
        giftCertificate.setPrice(new BigDecimal("123"));
        giftCertificate.setDuration(1);
        giftCertificate.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setTags(tags);

        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDao.findGiftCertificateTags(giftCertificate.getId())).thenReturn(tags);
        GiftCertificate mockedGiftCertificateDto = giftCertificateService.findGiftCertificateById(giftCertificate.getId());

        assertEquals(giftCertificate, mockedGiftCertificateDto);
    }

    @Test
    void whenFindGiftCertificateByIdThenShouldThrowException() {
        assertThrows(ValidationException.class, () -> giftCertificateService.findGiftCertificateById(-1L));
    }

    @Test
    void whenFindGiftCertificatesByParametersThenShouldReturnListOfGiftCertificate() {
        QueryParameter parameter = new QueryParameter("Hi", null, null, null, null);

        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Hi");
        tags.add(tag);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Hello");
        giftCertificate.setDescription("Hello from description");
        giftCertificate.setPrice(new BigDecimal("123"));
        giftCertificate.setDuration(1);
        giftCertificate.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setTags(tags);

        when(giftCertificateDao.findCertificatesByQueryParameters(anyString())).thenReturn(Collections.singletonList(giftCertificate));

        List<GiftCertificate > giftCertificateDtos = giftCertificateService.findGiftCertificatesByParameters(parameter);

        assertEquals(Collections.singletonList(giftCertificate), giftCertificateDtos);
    }

    @Test
    void whenFindGiftCertificatesByParametersThenShouldThrowException() {
        QueryParameter parameter = new QueryParameter("@@#", null, null, null, null);

        assertThrows(ValidationException.class, () -> giftCertificateService.findGiftCertificatesByParameters(parameter));
    }

    @Test
    void whenDeleteGiftCertificateByIdThenShouldNotThrowException() {
        Long certificateId = 1L;
        doNothing().when(giftCertificateDao).removeById(certificateId);

        assertDoesNotThrow(() -> giftCertificateService.deleteGiftCertificateById(certificateId));
    }

    @Test
    void whenDeleteGiftCertificateByIdThenShouldThrowException() {
        Long certificateId = -1L;
        doNothing().when(giftCertificateDao).removeById(certificateId);

        assertThrows(ValidationException.class, () -> giftCertificateService.deleteGiftCertificateById(certificateId));
    }

    @Test
    void whenUpdateGiftCertificateThenShouldReturnUpdatedGiftCertificate() {
        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Hi");
        tags.add(tag);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Hello");
        giftCertificate.setDescription("Hello from description");
        giftCertificate.setPrice(new BigDecimal("123"));
        giftCertificate.setDuration(1);
        giftCertificate.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificate.setTags(tags);

        GiftCertificate  giftCertificateDto = new GiftCertificate ();
        giftCertificateDto.setName("Hello");
        giftCertificateDto.setDescription("Hello from description");
        giftCertificateDto.setPrice(new BigDecimal("123"));
        giftCertificateDto.setDuration(1);
        giftCertificateDto.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificateDto.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        Set<Tag > tagsDto = new HashSet<>();
        Tag  tagDto = new Tag ();
        tagDto.setName("Hi");
        tagsDto.add(tagDto);
        giftCertificateDto.setTags(tagsDto);

        when(giftCertificateDao.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDao.update(giftCertificate)).thenReturn(giftCertificate);
        when(tagDao.findAll()).thenReturn(new LinkedList<>());
        when(tagDao.add(tag)).thenReturn(tag.getId());
        when(giftCertificateDao.findGiftCertificateTags(giftCertificate.getId())).thenReturn(tags);
        GiftCertificate  mockedGiftCertificate  = giftCertificateService
                .updateGiftCertificate(giftCertificate.getId(), giftCertificateDto);

        assertEquals(giftCertificate, mockedGiftCertificate );
    }

    @Test
    void whenUpdateGiftCertificateThenShouldReturnThrowException() {
        GiftCertificate  giftCertificateDto = new GiftCertificate ();
        giftCertificateDto.setName("Hello");
        giftCertificateDto.setDescription("Hello from description");
        giftCertificateDto.setPrice(new BigDecimal("123"));
        giftCertificateDto.setDuration(1);
        giftCertificateDto.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificateDto.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));

        assertThrows(ValidationException.class, () -> giftCertificateService.updateGiftCertificate(-123L, giftCertificateDto));
    }

}