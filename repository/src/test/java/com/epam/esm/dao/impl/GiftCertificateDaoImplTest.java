package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Aliaksei Halkin
 */
class GiftCertificateDaoImplTest {
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
    void whenAddCorrectGiftCertificateThenShouldReturnCorrectGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("TestCertificate");
        giftCertificate.setDescription("test description");
        giftCertificate.setPrice(new BigDecimal("13.0"));
        giftCertificate.setCreatedDate(LocalDateTime.now());
        giftCertificate.setUpdateDate(LocalDateTime.now());
        long actual = giftCertificateDao.add(giftCertificate);
        assertEquals(6, actual);
    }


}