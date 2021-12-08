package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The interface represents {@code GiftCertificate} dao
 *
 * @author Aliaksei Halkin
 */
public interface GiftCertificateDao extends BaseDao<GiftCertificate, Long> {
    List<GiftCertificate> findAll(Map<String, String> queryParameters);

    List<GiftCertificate> findCertificatesByQueryParameters(Map<String, String> query);

    Set<Tag> findGiftCertificateTags(long certificateId);

    void deactivate(GiftCertificate giftCertificate);

    Optional<GiftCertificate> findByName(String name);

    long countTotalRows(Map<String, String> queryParameters);
}
