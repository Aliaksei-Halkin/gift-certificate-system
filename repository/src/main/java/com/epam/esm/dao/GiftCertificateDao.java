package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Set;

/**
 * The interface represents {@code GiftCertificate} dao
 *
 * @author Aliaksei Halkin
 */
public interface GiftCertificateDao extends BaseDao<GiftCertificate, Long> {
    List<GiftCertificate> findCertificatesByQueryParameters(String query);

    Set<Tag> findGiftCertificateTags(long certificateId);

    void attachedTag(long tagId, long giftCertificateId);
}
