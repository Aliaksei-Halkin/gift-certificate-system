package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The interface represents {@code GiftCertificate} dao
 *
 * @author Aliaksei Halkin
 */
public interface GiftCertificateDao extends BaseDao<GiftCertificateEntity, Long> {
    List<GiftCertificateEntity> findAll(Map<String, String> queryParameters);

    List<GiftCertificateEntity> findCertificatesByQueryParameters(Map<String, String> query);

    Set<TagEntity> findGiftCertificateTags(long certificateId);

    void deactivate(GiftCertificateEntity giftCertificate);

    Optional<GiftCertificateEntity> findByName(String name);

    long countTotalRows(Map<String, String> queryParameters);
}
