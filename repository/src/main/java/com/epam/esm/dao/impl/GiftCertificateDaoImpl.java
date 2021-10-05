package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Aliaksei Halkin
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateMapper giftCertificateMapper;
    private final TagMapper tagMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, GiftCertificateMapper giftCertificateMapper,
                                  TagMapper tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftCertificateMapper = giftCertificateMapper;
        this.tagMapper = tagMapper;
    }


    @Override
    public Optional findById(long id) {
        return Optional.empty();
    }

    @Override
    public long add(Object entity) {
        return 0;
    }

    @Override
    public void removeById(long id) {

    }

    @Override
    public Object update(Object entity) {
        return null;
    }

    @Override
    public List<GiftCertificate> findCertificatesByQueryParameters(String query) {
        return null;
    }

    @Override
    public Set<Tag> findGiftCertificateTags(long certificateId) {
        return null;
    }

    @Override
    public void addRelationBetweenTagAndCertificate(long tagId, long giftCertificateId) {

    }
}
