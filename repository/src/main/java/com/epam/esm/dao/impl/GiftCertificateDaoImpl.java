package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Aliaksei Halkin
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    public static final String SELECT_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificates WHERE certificateId = ?";
    public static final String SELECT_CERTIFICATES_BY_PARAMETERS =
            "SELECT DISTINCT gift_certificates.certificateId, gift_certificates.name, gift_certificates.description, " +
                    "gift_certificates.price, gift_certificates.duration, gift_certificates.create_date, " +
                    "gift_certificates.last_update_date FROM gift_certificates ";
    public static final String SELECT_CERTIFICATE_TAGS =
            "SELECT tags.tagId, tags.tagName " +
                    "FROM tags " +
                    "JOIN certificates_has_tags ON tags.tagId = certificates_has_tags.tagId " +
                    "JOIN gift_certificates ON gift_certificates.certificateId = certificates_has_tags.certificateId " +
                    "WHERE gift_certificates.certificateId = ?";
    public static final String INSERT_CERTIFICATE =
            "INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_CERTIFICATE =
            "UPDATE gift_certificates " +
                    "SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ? " +
                    "WHERE certificateId = ?";
    public static final String DELETE_CERTIFICATE =
            "DELETE FROM gift_certificates WHERE certificateId = ?";
    public static final String INSERT_RELATION_BETWEEN_TAG_AND_GIFT_CERTIFICATE =
            "INSERT INTO certificates_has_tags (certificateId, tagId) VALUES (?, ?)";
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
    public Optional<GiftCertificate> findById(Long id) {
        return jdbcTemplate.query(SELECT_CERTIFICATE_BY_ID, giftCertificateMapper, id).stream().findFirst();
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(SELECT_CERTIFICATES_BY_PARAMETERS, giftCertificateMapper);
    }

    @Override
    public long add(GiftCertificate entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setBigDecimal(3, entity.getPrice());
            preparedStatement.setInt(4, entity.getDuration());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getCreatedDate()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getUpdateDate()));
            return preparedStatement;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().longValue();
        }
        throw new RuntimeException("Generated id not found");
    }

    @Override
    public void removeById(Long id) {
        jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) {
        jdbcTemplate.update(UPDATE_CERTIFICATE, entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration(), Timestamp.valueOf(entity.getCreatedDate()), Timestamp.valueOf(entity.getUpdateDate()),
                entity.getId());
        return entity;
    }

    @Override
    public List<GiftCertificate> findCertificatesByQueryParameters(String query) {
        return jdbcTemplate.query(SELECT_CERTIFICATES_BY_PARAMETERS + query, giftCertificateMapper);
    }

    @Override
    public Set<Tag> findGiftCertificateTags(long certificateId) {
        return new HashSet<>(jdbcTemplate.query(SELECT_CERTIFICATE_TAGS, tagMapper, certificateId));
    }

    @Override
    public void addRelationBetweenTagAndCertificate(long tagId, long giftCertificateId) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT_RELATION_BETWEEN_TAG_AND_GIFT_CERTIFICATE);
            preparedStatement.setLong(1, giftCertificateId);
            preparedStatement.setLong(2, tagId);
            return preparedStatement;
        });
    }
}
