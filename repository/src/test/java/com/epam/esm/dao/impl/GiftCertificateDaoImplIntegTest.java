package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.mapper.ColumnName;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliaksei Halkin
 */
class GiftCertificateDaoImplIntegTest {
    private EmbeddedDatabase database;
    private GiftCertificateDao giftCertificateDao;

    @BeforeEach
    void setData() {
        database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql").addScript("classpath:test-data.sql").build();
        giftCertificateDao = new GiftCertificateDaoImpl(new JdbcTemplate(database), new GiftCertificateMapper(),
                new TagMapper());
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
        giftCertificateDao = null;
    }

    @Test
    void when_AddCorrectGiftCertificate_ThenShouldReturn_CorrectGiftCertificate() {
        String nameGiftCertificate = "TestCertificate";
        String addingQuery = " WHERE " + ColumnName.NAME + " = \'" + nameGiftCertificate + "\'";
        int firstGiftCertificate = 0;
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(nameGiftCertificate);
        giftCertificate.setDescription("test description");
        giftCertificate.setPrice(new BigDecimal("13.0"));
        giftCertificate.setCreatedDate(LocalDateTime.now());
        giftCertificate.setUpdateDate(LocalDateTime.now());
        long actual = giftCertificateDao.add(giftCertificate);
        long expected = giftCertificateDao.findCertificatesByQueryParameters(addingQuery)
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
    void when_FindByExistId_ThenShoul_dReturnTrue() {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(1L);
        assertTrue(giftCertificateOptional.isPresent());
    }

    @Test
    void when_GetByNoExistId_ThenShould_ReturnFalse() {
        giftCertificateDao.removeById(10L);
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(10L);
        assertFalse(giftCertificateOptional.isPresent());
    }

    @Test
    void whenRemoveByExistIdThenShouldListCertificatesLessOne() {
        List<GiftCertificate> giftCertificateList = giftCertificateDao.findCertificatesByQueryParameters("");
        int expected = giftCertificateList.size();
        giftCertificateDao.removeById(1L);
        List<GiftCertificate> giftCertificatesAfterRemove = giftCertificateDao.findCertificatesByQueryParameters("");
        int actual = giftCertificatesAfterRemove.size();
        assertNotEquals(expected, actual);
    }

    @Test
    void whenUpdateGiftCertificateThenShouldReturnUpdatedCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate(1L, "Sauna", "Russian sauna",
                new BigDecimal(25.22), 22, LocalDateTime.now(), LocalDateTime.now(), new HashSet<Tag>() {
        });
        GiftCertificate updatedGiftCertificate = giftCertificateDao.update(giftCertificate);
        assertEquals(giftCertificate, updatedGiftCertificate);
    }

    @Test
    void whenUpdateIncorrectGiftCertificateThenShouldThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate(1L, null, "American sauna",
                new BigDecimal(15.22), 22, LocalDateTime.now(), LocalDateTime.now(), new HashSet<Tag>() {
        });
        assertThrows(RuntimeException.class, () -> giftCertificateDao.update(giftCertificate));
    }

    @Test
    void whenFindCertificatesByQueryParametersThenShouldReturnListCertificates() {
        List<GiftCertificate> allCertificates = giftCertificateDao.findCertificatesByQueryParameters("");
        assertEquals(5, allCertificates.size());
    }

    @Test
    void whenFindCertificatesByQueryParametersThenShouldThrowException() {
        assertThrows(BadSqlGrammarException.class, () -> giftCertificateDao.findCertificatesByQueryParameters("helloJDBC"));
    }

    @Test
    void whenFindGiftCertificateTagsThenShouldReturnSetTags() {
        Set<Tag> tagSet = giftCertificateDao.findGiftCertificateTags(1);
        assertEquals(3, tagSet.size());
    }

    @Test
    void whenAddRelationBetweenTagAndGiftCertificateThenShouldNotThrowException() {
        assertDoesNotThrow(() -> giftCertificateDao.addRelationBetweenTagAndGiftCertificate(2, 2));
    }

    @Test
    void whenAddRelationBetweenTagAndGiftCertificateThenShouldThrowException() {
        assertThrows(DuplicateKeyException.class, () -> giftCertificateDao.addRelationBetweenTagAndGiftCertificate(1, 2));
    }
}
