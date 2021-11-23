package com.epam.esm.dao.impl;

import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
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

//    @Test
//    void when_AddCorrectGiftCertificate_ThenShouldReturn_CorrectGiftCertificate() {
//        String nameGiftCertificate = "TestCertificate";
//        Map<String,String> queryParameters =new HashMap<>();
//        queryParameters.put("name",nameGiftCertificate);
//        int firstGiftCertificate = 0;
//        GiftCertificate giftCertificate = new GiftCertificate();
//        giftCertificate.setName(nameGiftCertificate);
//        giftCertificate.setDescription("test description");
//        giftCertificate.setPrice(new BigDecimal("13.0"));
//        giftCertificate.setCreatedDate(LocalDateTime.now());
//        giftCertificate.setUpdateDate(LocalDateTime.now());
//        long actual = giftCertificateDao.add(giftCertificate);
//        long expected = giftCertificateDao.findCertificatesByQueryParameters(queryParameters)
//                .get(firstGiftCertificate).getId();
//        assertEquals(expected, actual);
//    }

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
    void when_FindByExistId_ThenShoul_dReturnTrue() {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(1L);
        assertTrue(giftCertificateOptional.isPresent());
    }

//    @Test
//    void when_GetByNoExistId_ThenShould_ReturnFalse() {
//        giftCertificateDao.deactivate(1);
//        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(10L);
//        assertFalse(giftCertificateOptional.isPresent());
//    }

    @Test
    void whenRemoveByExistIdThenShouldListCertificatesLessOne() {
        Optional<GiftCertificate> certificateById = giftCertificateDao.findById(1L);
        boolean expected = certificateById.get().isActive();
        giftCertificateDao.deactivate(certificateById.get());
        Optional<GiftCertificate> certificateByIdActual = giftCertificateDao.findById(1L);
        boolean actual = certificateByIdActual.get().isActive();
        assertNotEquals(expected, actual);
    }

//    @Test
//    void whenUpdateGiftCertificateThenShouldReturnUpdatedCertificate() {
//        GiftCertificate giftCertificate = new GiftCertificate(1L, "Sauna", "Russian sauna",
//                new BigDecimal(25.22), 22, LocalDateTime.now(), LocalDateTime.now(), new HashSet<Tag>() {
//        }, true);
//        GiftCertificate updatedGiftCertificate = giftCertificateDao.update(giftCertificate);
//        assertEquals(giftCertificate, updatedGiftCertificate);
//    }

    @Test
    void whenUpdateIncorrectGiftCertificateThenShouldThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate(1L, null, "American sauna",
                new BigDecimal(15.22), 22, LocalDateTime.now(), LocalDateTime.now(), new HashSet<Tag>() {
        }, true);
        assertThrows(RuntimeException.class, () -> giftCertificateDao.update(giftCertificate));
    }

//    @Test
//    void whenFindCertificatesByQueryParametersThenShouldReturnListCertificates() {
//        List<GiftCertificate> allCertificates = giftCertificateDao.findCertificatesByQueryParameters("");
//        assertEquals(5, allCertificates.size());
//    }
//
//    @Test
//    void whenFindCertificatesByQueryParametersThenShouldThrowException() {
//        assertThrows(BadSqlGrammarException.class, () -> giftCertificateDao.findCertificatesByQueryParameters("helloJDBC"));
//    }

//    @Test
//    void whenFindGiftCertificateTagsThenShouldReturnSetTags() {
//        Set<Tag> tagSet = giftCertificateDao.findGiftCertificateTags(1);
//        assertEquals(3, tagSet.size());
//    }

    @Test
    void whenAddRelationBetweenTagAndGiftCertificateThenShouldNotThrowException() {
        assertDoesNotThrow(() -> giftCertificateDao.attachTag(2, 2));
    }

//    @Test
//    void whenAddRelationBetweenTagAndGiftCertificateThenShouldThrowException() {
//        assertThrows(DuplicateKeyException.class, () -> giftCertificateDao.attachTag(1, 2));
//    }
}
