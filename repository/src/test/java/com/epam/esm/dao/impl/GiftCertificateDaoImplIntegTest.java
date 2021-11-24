package com.epam.esm.dao.impl;

import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
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
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(nameGiftCertificate);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(new BigDecimal("13.0"));
        giftCertificate.setActive(true);
        long actual = giftCertificateDao.add(giftCertificate);
        List<GiftCertificate> certificatesByQueryParameters =
                giftCertificateDao.findCertificatesByQueryParameters(queryParameters);
        long expected = certificatesByQueryParameters
                .get(firstGiftCertificate).getId();
        assertEquals(expected, actual);
    }

    @Test
    void when_AddIncorrectGiftCertificate_ThenShould_ThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(null);
        giftCertificate.setDescription("test description");
        giftCertificate.setPrice(new BigDecimal("13.0"));
        giftCertificate.setCreatedDate(LocalDateTime.now());
        giftCertificate.setUpdateDate(LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> giftCertificateDao.add(giftCertificate));
    }

    @Test
    void when_FindByExistId_ThenShould_ReturnTrue() {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(1L);
        assertTrue(giftCertificateOptional.isPresent());
    }

    @Test
    void when_GetByNoExistId_ThenShould_ReturnFalse() {
        long id = 10L;
        Optional<GiftCertificate> giftCertificate = giftCertificateDao.findById(id);
        if (giftCertificate.isPresent()) {
            giftCertificateDao.deactivate(giftCertificate.get());
            Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(10L);
            assertFalse(giftCertificateOptional.isPresent());
        }
    }

    @Test
    void when_RemoveByExistId_ThenShouldReturn_ListCertificates_LessOne() {
        HashMap<String, String> param = new HashMap<>();
        param.put("page", "1");
        param.put("per_page", "500");
        List<GiftCertificate> allGiftCertificate = giftCertificateDao.findAll(param);
        int expected = allGiftCertificate.size();
        Optional<GiftCertificate> certificateById = giftCertificateDao.findById(1L);
        if (certificateById.isPresent()) {
            GiftCertificate giftCertificate = certificateById.get();
            giftCertificateDao.deactivate(giftCertificate);
        }
        int actual = giftCertificateDao.findAll(param).size();
        assertEquals(expected, actual + 1);
    }

    @Test
    void whenUpdateGiftCertificateThenShouldReturnUpdatedCertificate() {
        long id = 1L;
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate oldCertificate = optionalGiftCertificate.get();
            oldCertificate.setDescription("new new new new");
            GiftCertificate updatedGiftCertificate = giftCertificateDao.update(oldCertificate);
            assertEquals(oldCertificate, updatedGiftCertificate);
        }
    }

    @Test
    void whenUpdateIncorrectGiftCertificateThenShouldThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate(1L, null, "American sauna",
                new BigDecimal(15.22), 22, LocalDateTime.now(), LocalDateTime.now(), new HashSet<Tag>() {
        }, true);
        assertThrows(RuntimeException.class, () -> giftCertificateDao.update(giftCertificate));
    }

    @Test
    void when_FindCertificatesByQueryParameters_ThenShouldReturn_ListCertificates() {
        Map<String, String> param = new HashMap<>();
        param.put("page", "1");
        param.put("per_page", "5");
        List<GiftCertificate> allCertificates = giftCertificateDao.findCertificatesByQueryParameters(param);
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
        Set<Tag> tagSet = giftCertificateDao.findGiftCertificateTags(4);
        assertEquals(3, tagSet.size());
    }
}
