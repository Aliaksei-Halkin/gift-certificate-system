package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class represents GiftCertificate dao implementation.
 * This class use {@link JdbcTemplate} to do standard CRUD operations in a database with table GiftCertificate.
 *
 * @author Aliaksei Halkin
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    public static final String SELECT_CERTIFICATES_BY_PARAMETERS =
            "SELECT DISTINCT gift_certificates.certificateId, gift_certificates.name, gift_certificates.description, " +
                    "gift_certificates.price, gift_certificates.duration, gift_certificates.create_date, " +
                    "gift_certificates.last_update_date, gift_certificates.active FROM gift_certificates ";
    public static final String SELECT_CERTIFICATE_TAGS =
            "SELECT tags FROM GiftCertificate WHERE GiftCertificate.id = ?1";
    public static final String DELETE_CERTIFICATE =
            "UPDATE GiftCertificate SET active=false WHERE id = ?1";
    public static final String RETURN_DELETED_CERTIFICATE =
            "UPDATE GiftCertificate  SET active=true WHERE name = ?1";
        private static final String SELECT_ALL_CERTIFICATES = "SELECT g FROM GiftCertificate g";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The method find  certificate by id
     *
     * @param id {@code Long} the id of the GiftCertificate
     * @return Optional with {@link GiftCertificate} entity
     */
    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    /**
     * The method find all gift certificates
     *
     * @return {@code List} of all certificates
     */
    @Override
    public List<GiftCertificate> findAll() {
        return entityManager.createQuery(SELECT_ALL_CERTIFICATES, GiftCertificate.class).getResultList();
    }

    /**
     * The method add gift certificate to database
     *
     * @return {@code long} id of added certificate
     */
    @Override
    public long add(GiftCertificate entity) {
        entityManager.persist(entity);
        return entity.getId();
    }

    /**
     * The method delete certificate by id
     *
     * @param id of certificate
     */
    @Override
    public void removeById(Long id) {
        entityManager.createQuery(DELETE_CERTIFICATE)
                .setParameter(1, id).executeUpdate();
    }

    /**
     * The method update certificate by id
     *
     * @param entity GiftCertificate for update
     * @return updating GiftCertificate
     */
    @Override
    public GiftCertificate update(GiftCertificate entity) {
        GiftCertificate updatedGiftCertificate = entityManager.merge(entity);
        entityManager.flush();
        entityManager.clear();
        return updatedGiftCertificate;
    }

    /**
     * The method update certificate by id
     *
     * @param query represents additional query to main quiery {@code SELECT_CERTIFICATES_BY_PARAMETERS}
     * @return {@code List} of certificates
     */
    @Override
    public List<GiftCertificate> findCertificatesByQueryParameters(String query) {//todo
   //     return jdbcTemplate.query(SELECT_CERTIFICATES_BY_PARAMETERS + query, giftCertificateMapper);
        return new ArrayList<>();
    }

    /**
     * The method find all tags for certificate
     *
     * @param certificateId {@code long} id of certificate
     * @return {@code Set} of all tags
     */
    @Override
    public Set<Tag> findGiftCertificateTags(long certificateId) {
        return (Set<Tag>) entityManager.createQuery(SELECT_CERTIFICATE_TAGS)
                .setParameter(1, certificateId).getResultStream().collect(Collectors.toSet());

    }

    /**
     * The method add certificate and tags to certificate_has_tag table.
     *
     * @param tagId             id of tag
     * @param giftCertificateId id of certificate
     */
    @Override
    public void attachTag(long tagId, long giftCertificateId) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, giftCertificateId);
        Tag tag = entityManager.find(Tag.class, tagId);
        Set<Tag> tags = giftCertificate.getTags();
        tags.add(tag);
        giftCertificate.setTags(tags);
        entityManager.merge(giftCertificate);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public void activateGiftCertificate(String name) {
        entityManager.createQuery(RETURN_DELETED_CERTIFICATE).setParameter(1, name).executeUpdate();
    }

    @Override
    public void removeTag(long giftCertificateId, long tagId) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, giftCertificateId);
        Set<Tag> tags = giftCertificate.getTags();
        Set<Tag> tagsUpdate = tags.stream().filter(tag -> tag.getId() != tagId).collect(Collectors.toSet());
        giftCertificate.setTags(tagsUpdate);
        entityManager.merge(giftCertificate);
        entityManager.flush();
        entityManager.clear();
    }

}
