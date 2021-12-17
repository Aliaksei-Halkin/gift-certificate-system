package com.epam.esm.dao.impl;

import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliaksei Halkin
 */
@SpringBootTest(classes = {RepositoryTestConfig.class})
@ActiveProfiles("test")
@Transactional
class GiftCertificateDaoImplIntegTest {
    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Test
    void when_AddCorrectGiftCertificate_ThenShouldReturn_CorrectGiftCertificate() {
        String nameGiftCertificate = "TestCertificate UNIQUE UNIQUE UNIQUE";
        String description = "test description";
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("name", nameGiftCertificate);
        queryParameters.put("page", "1");
        queryParameters.put("per_page", "100");
        int firstGiftCertificate = 0;
        GiftCertificateEntity giftCertificate = new GiftCertificateEntity();
        giftCertificate.setName(nameGiftCertificate);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(new BigDecimal("13.0"));
        giftCertificate.setActive(true);
        long actual = giftCertificateDao.add(giftCertificate);
        List<GiftCertificateEntity> certificatesByQueryParameters =
                giftCertificateDao.findCertificatesByQueryParameters(queryParameters);
        long expected = certificatesByQueryParameters
                .get(firstGiftCertificate).getId();
        assertEquals(expected, actual);
    }

    @Test
    void when_AddIncorrectGiftCertificate_ThenShould_ThrowException() {
        GiftCertificateEntity giftCertificate = new GiftCertificateEntity();
        giftCertificate.setName(null);
        giftCertificate.setDescription("test description");
        giftCertificate.setPrice(new BigDecimal("13.0"));
        giftCertificate.setCreatedDate(LocalDateTime.now());
        giftCertificate.setUpdateDate(LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> giftCertificateDao.add(giftCertificate));
    }

    @Test
    void when_FindByExistId_ThenShould_ReturnTrue() {
        Optional<GiftCertificateEntity> giftCertificateOptional = giftCertificateDao.findById(1L);
        assertTrue(giftCertificateOptional.isPresent());
    }

    @Test
    void when_GetByNoExistId_ThenShould_ReturnFalse() {
        long id = 10L;
        Optional<GiftCertificateEntity> giftCertificate = giftCertificateDao.findById(id);
        if (giftCertificate.isPresent()) {
            giftCertificateDao.deactivate(giftCertificate.get());
            Optional<GiftCertificateEntity> giftCertificateOptional = giftCertificateDao.findById(10L);
            assertFalse(giftCertificateOptional.isPresent());
        }
    }

    @Test
    void when_RemoveByExistId_ThenShouldReturn_ListCertificates_LessOne() {
        HashMap<String, String> param = new HashMap<>();
        param.put("page", "1");
        param.put("per_page", "500");
        List<GiftCertificateEntity> allGiftCertificate = giftCertificateDao.findAll(param);
        int expected = allGiftCertificate.size();
        Optional<GiftCertificateEntity> certificateById = giftCertificateDao.findById(1L);
        if (certificateById.isPresent()) {
            GiftCertificateEntity giftCertificate = certificateById.get();
            giftCertificateDao.deactivate(giftCertificate);
        }
        int actual = giftCertificateDao.findAll(param).size();
        assertEquals(expected, actual + 1);
    }

    @Test
    void whenUpdateGiftCertificateThenShouldReturnUpdatedCertificate() {
        long id = 1L;
        Optional<GiftCertificateEntity> optionalGiftCertificate = giftCertificateDao.findById(id);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificateEntity oldCertificate = optionalGiftCertificate.get();
            oldCertificate.setDescription("new new new new");
            GiftCertificateEntity updatedGiftCertificate = giftCertificateDao.update(oldCertificate);
            assertEquals(oldCertificate, updatedGiftCertificate);
        }
    }

    @Test
    void whenUpdateIncorrectGiftCertificateThenShouldThrowException() {
        GiftCertificateEntity giftCertificate = new GiftCertificateEntity(1L, null, "American sauna",
                new BigDecimal(15.22), 22, LocalDateTime.now(), LocalDateTime.now(), new HashSet<TagEntity>() {
        }, true);
        assertThrows(RuntimeException.class, () -> giftCertificateDao.update(giftCertificate));
    }

    @Test
    void when_FindCertificatesByQueryParameters_ThenShouldReturn_ListCertificates() {
        Map<String, String> param = new HashMap<>();
        param.put("page", "1");
        param.put("per_page", "5");
        List<GiftCertificateEntity> allCertificates = giftCertificateDao.findCertificatesByQueryParameters(param);
        assertEquals(5, allCertificates.size());
    }

    @Test
    void when_FindCertificatesByQueryParameters_ThenShould_ThrowException() {
        Map<String, String> param = new HashMap<>();
        param.put("page", "-5");
        assertThrows(Exception.class, () -> giftCertificateDao.findCertificatesByQueryParameters(param));
    }

    @Test
    void whenFindGiftCertificateTagsThenShouldReturnSetTags() {
        Set<TagEntity> tagSet = giftCertificateDao.findGiftCertificateTags(4);
        assertEquals(3, tagSet.size());
    }
}
