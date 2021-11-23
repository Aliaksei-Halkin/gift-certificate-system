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
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    private GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao,
            new GiftCertificateValidator(giftCertificateDao), new TagValidator());
    private GiftCertificate giftCertificateFirst = new GiftCertificate();
    private GiftCertificate giftCertificateSecond = new GiftCertificate();
    private Tag tag = new Tag();


    @BeforeEach
    void beforeAll() {
        giftCertificateFirst.setName("Hello");
        giftCertificateFirst.setDescription("Hello from description");
        giftCertificateFirst.setPrice(new BigDecimal("123"));
        giftCertificateFirst.setDuration(1);
        giftCertificateFirst.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificateFirst.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificateFirst.setActive(true);
        Set<Tag> tags = new HashSet<>();
        tag.setId(1L);
        tag.setName("Hi");
        tags.add(tag);
        giftCertificateFirst.setTags(tags);
        giftCertificateSecond.setId(1L);
        giftCertificateSecond.setName("Hello");
        giftCertificateSecond.setDescription("Hello from description");
        giftCertificateSecond.setPrice(new BigDecimal("123"));
        giftCertificateSecond.setDuration(1);
        giftCertificateSecond.setCreatedDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificateSecond.setUpdateDate(LocalDateTime.of(2012, 12, 2, 14, 56, 44));
        giftCertificateSecond.setTags(tags);
        giftCertificateSecond.setActive(true);
    }

    @Test
    void when_addGiftCertificate_ThenShouldReturnGiftCertificate() {
        when(giftCertificateDao.add(any(GiftCertificate.class))).thenReturn(giftCertificateSecond.getId());
        when(tagDao.findTagByName(tag.getName())).thenReturn(Optional.of(tag));
        GiftCertificate mockedGiftCertificate = giftCertificateService.addGiftCertificate(giftCertificateSecond);
        giftCertificateSecond.setCreatedDate(mockedGiftCertificate.getCreatedDate());
        giftCertificateSecond.setUpdateDate(mockedGiftCertificate.getUpdateDate());
        verify(giftCertificateDao, Mockito.times(1)).add(any(GiftCertificate.class));
        verify(tagDao, Mockito.times(1)).findTagByName(tag.getName());
        assertEquals(giftCertificateSecond, mockedGiftCertificate);
    }

    @Test
    void when_addGiftCertificate_ThenShouldThrowException() {
        when(giftCertificateDao.add(any(GiftCertificate.class))).thenReturn(giftCertificateSecond.getId());
        when(tagDao.findTagByName(tag.getName())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.addGiftCertificate(giftCertificateFirst));
        verify(giftCertificateDao, Mockito.times(1)).add(any(GiftCertificate.class));
        verify(tagDao, Mockito.times(1)).findTagByName(tag.getName());
    }

    @Test
    void when_AddTagToGiftCertificate_ThenShouldReturnGiftCertificate() {
        Tag tagDto = new Tag();
        tagDto.setName("Hi");
        when(giftCertificateDao.findById(giftCertificateSecond.getId())).thenReturn(Optional.of(giftCertificateSecond));
        when(giftCertificateDao.update(giftCertificateSecond)).thenReturn(giftCertificateSecond);
        when(giftCertificateDao.findGiftCertificateTags(giftCertificateSecond.getId())).thenReturn(giftCertificateSecond.getTags());
        when(tagDao.findTagByName(tag.getName())).thenReturn(Optional.ofNullable(tag));
        GiftCertificate mockedGiftCertificate = giftCertificateService.addTagToGiftCertificate(giftCertificateSecond.getId(), tag);
        verify(giftCertificateDao).findById(anyLong());
        verify(tagDao, Mockito.times(1)).findTagByName(anyString());
        verify(giftCertificateDao).update(any(GiftCertificate.class));
        verify(giftCertificateDao, Mockito.times(2)).findGiftCertificateTags(anyLong());
        assertEquals(giftCertificateSecond, mockedGiftCertificate);
    }

    @Test
    void when_AddTagToGiftCertificate_ThenShouldThrowException() {
        assertThrows(ResourceNotFoundException.class,
                () -> giftCertificateService.addTagToGiftCertificate(giftCertificateSecond.getId(), tag));
    }

    @Test
    void when_FindGiftCertificateById_ThenShouldReturnGiftCertificate() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(giftCertificateSecond));
        when(giftCertificateDao.findGiftCertificateTags(giftCertificateSecond.getId())).thenReturn(giftCertificateSecond.getTags());
        GiftCertificate mockedGiftCertificate = giftCertificateService.findGiftCertificateById(giftCertificateSecond.getId());
        verify(giftCertificateDao).findById(anyLong());
        verify(giftCertificateDao).findGiftCertificateTags(anyLong());
        assertEquals(giftCertificateSecond, mockedGiftCertificate);
    }

    @Test
    void when_FindGiftCertificateById_ThenShouldThrowException() {
        assertThrows(ValidationException.class, () -> giftCertificateService.findGiftCertificateById(-1L));
    }

//    @Test
//    void when_FindGiftCertificatesByParameters_ThenShouldReturnListOfGiftCertificate() {
//        QueryParameter parameter = new QueryParameter("Hi", null, null, null );
//        when(giftCertificateDao.findCertificatesByQueryParameters(anyString())).thenReturn(Collections.singletonList(giftCertificateSecond));
//        List<GiftCertificate> giftCertificateDtos = giftCertificateService.findGiftCertificatesByParameters(parameter);
//        verify(giftCertificateDao).findCertificatesByQueryParameters(anyString());
//        assertEquals(Collections.singletonList(giftCertificateSecond), giftCertificateDtos);
//    }

//    @Test
//    void when_FindGiftCertificatesByParameters_ThenShouldThrowException() {
//        QueryParameter parameter = new QueryParameter("@@#", null, null, null );
//        assertThrows(ValidationException.class, () -> giftCertificateService.findGiftCertificatesByParameters(parameter));
//    }

//    @Test
//    void when_DeleteGiftCertificateById_ThenShouldNotThrowException() {
//        Long certificateId = giftCertificateSecond.getId();
//        when(giftCertificateDao.findAll()).thenReturn(Collections.singletonList(giftCertificateSecond));
//        when(giftCertificateDao.findById(certificateId)).thenReturn(Optional.of(giftCertificateSecond));
//        //todo will change id on entity doNothing().when(giftCertificateDao).removeById(certificateId);
//        assertDoesNotThrow(() -> giftCertificateService.deleteGiftCertificateById(certificateId));
//    }

    @Test
    void when_DeleteGiftCertificateById_ThenShouldThrowException() {
        Long certificateId = -1L;
        //todo will change id on entity  doNothing().when(giftCertificateDao).removeById(certificateId);
        assertThrows(ValidationException.class, () -> giftCertificateService.deleteGiftCertificateById(certificateId));
    }

    @Test
    void when_UpdateGiftCertificate_ThenShouldReturnUpdatedGiftCertificate() {
        when(giftCertificateDao.findById(giftCertificateSecond.getId())).thenReturn(Optional.of(giftCertificateSecond));
        when(giftCertificateDao.update(giftCertificateSecond)).thenReturn(giftCertificateSecond);
        when(giftCertificateDao.findGiftCertificateTags(giftCertificateSecond.getId())).thenReturn(giftCertificateSecond.getTags());
        when(tagDao.findTagByName(tag.getName())).thenReturn(Optional.of(tag));
        GiftCertificate mockedGiftCertificate = giftCertificateService
                .updateGiftCertificate(giftCertificateSecond.getId(), giftCertificateSecond);
        verify(tagDao, Mockito.times(1)).findTagByName(anyString());
        verify(giftCertificateDao,Mockito.times(2)).findById(anyLong());
        verify(giftCertificateDao).update(any(GiftCertificate.class));
        verify(giftCertificateDao, Mockito.times(3)).findGiftCertificateTags(anyLong());
        assertEquals(giftCertificateSecond, mockedGiftCertificate);
    }

    @Test
    void when_UpdateGiftCertificate_ThenShouldReturnThrowException() {
        assertThrows(ValidationException.class, () -> giftCertificateService.updateGiftCertificate(-123L, giftCertificateFirst));
    }

}