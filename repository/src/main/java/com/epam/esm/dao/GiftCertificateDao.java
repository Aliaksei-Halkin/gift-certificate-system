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
    List<GiftCertificate> findCertificatesByQueryParameters(Map<String, String> query);

    Set<Tag> findGiftCertificateTags(long certificateId);

    void attachTag(long tagId, long giftCertificateId);

    void activateGiftCertificate(String name);

    void removeTag(long giftCertificateId, long tagId);

    void deactivate(GiftCertificate giftCertificate);

    Optional<GiftCertificate> findCertificateByName(String name);
}
