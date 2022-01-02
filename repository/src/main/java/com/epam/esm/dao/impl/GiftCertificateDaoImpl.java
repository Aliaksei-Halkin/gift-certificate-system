package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.util.QueryBuilder;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class represents GiftCertificate dao implementation.
 * This class use JPA to do standard CRUD operations in a database with table GiftCertificate.
 *
 * @author Aliaksei Halkin
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String PAGE = "page";
    private static final String PER_PAGE = "per_page";
    public static final String SELECT_CERTIFICATE_TAGS =
            "SELECT tags FROM GiftCertificateEntity WHERE GiftCertificateEntity.id = ?1";
    private static final String SELECT_ALL_CERTIFICATES = "SELECT g FROM GiftCertificateEntity g ";
    private static final String SELECT_CERTIFICATE_BY_NAME = "FROM GiftCertificateEntity WHERE name = ?1";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The method find  certificate by id
     *
     * @param id {@code Long} the id of the GiftCertificate
     * @return Optional with {@link GiftCertificateEntity} entity
     */
    @Override
    public Optional<GiftCertificateEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificateEntity.class, id));
    }

    /**
     * The method find all gift certificates
     *
     * @return {@code List} of all certificates
     */
    @Override
    public List<GiftCertificateEntity> findAll(Map<String, String> queryParameters) {
        int page = Integer.parseInt(queryParameters.get(PAGE));
        int perPage = Integer.parseInt(queryParameters.get(PER_PAGE));
        int firstResult = page == 1 ? 0 : page * perPage - perPage;
        return entityManager.createQuery(SELECT_ALL_CERTIFICATES, GiftCertificateEntity.class)
                .setFirstResult(firstResult)
                .setMaxResults(perPage)
                .getResultList();
    }

    /**
     * The method add gift certificate to database
     *
     * @return {@code long} id of added certificate
     */
    @Override
    public long add(GiftCertificateEntity entity) {
        entityManager.persist(entity);
        return entity.getId();
    }

    /**
     * The method update certificate by id
     *
     * @param entity GiftCertificate for update
     * @return updating GiftCertificate
     */
    @Override
    public GiftCertificateEntity update(GiftCertificateEntity entity) {
        GiftCertificateEntity updatedGiftCertificate = entityManager.merge(entity);
        Hibernate.initialize(updatedGiftCertificate.getTags());
        entityManager.flush();
        entityManager.clear();
        return updatedGiftCertificate;
    }

    /**
     * The method update certificate by id
     *
     * @param queryParameters represents additional query to main quiery {@code SELECT_CERTIFICATES_BY_PARAMETERS}
     * @return {@code List} of certificates
     */
    @Override
    public List<GiftCertificateEntity> findCertificatesByQueryParameters(Map<String, String> queryParameters) {
        int page = Integer.parseInt(queryParameters.get(PAGE));
        int perPage = Integer.parseInt(queryParameters.get(PER_PAGE));
        int firstResult = page == 1 ? 0 : page * perPage - perPage;
        String query = SELECT_ALL_CERTIFICATES + QueryBuilder.createQueryForCertificates(queryParameters);
        return entityManager.createQuery(query, GiftCertificateEntity.class)
                .setFirstResult(firstResult).setMaxResults(perPage).getResultList();
    }

    /**
     * The method find all tags for certificate
     *
     * @param certificateId {@code long} id of certificate
     * @return {@code Set} of all tags
     */
    @Override
    public Set<TagEntity> findGiftCertificateTags(long certificateId) {
        return (Set<TagEntity>) entityManager.createQuery(SELECT_CERTIFICATE_TAGS)
                .setParameter(1, certificateId).getResultStream().collect(Collectors.toSet());
    }


    /**
     * The method deactivate certificate by id
     *
     * @param giftCertificate - the certificate
     */
    @Override
    public void deactivate(GiftCertificateEntity giftCertificate) {
        giftCertificate.setActive(false);
        entityManager.merge(giftCertificate);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public Optional<GiftCertificateEntity> findByName(String name) {
        return entityManager.createQuery(SELECT_CERTIFICATE_BY_NAME, GiftCertificateEntity.class)
                .setParameter(1, name).getResultStream()
                .findFirst();
    }

    @Override
    public long countTotalRows(Map<String, String> queryParameters) {
        String query = SELECT_ALL_CERTIFICATES + QueryBuilder.createQueryForCertificates(queryParameters);
        return entityManager.createQuery(query).getResultStream().count();
    }
}
