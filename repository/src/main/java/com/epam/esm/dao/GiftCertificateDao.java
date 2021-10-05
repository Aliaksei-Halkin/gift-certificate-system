package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Set;

/**
 * @author Aliaksei Halkin
 */
public interface GiftCertificateDao extends BaseDao {
    List<GiftCertificate> findCertificatesByQueryParameters(String query);

    Set<Tag> findGiftCertificateTags(long certificateId);

    void addRelationBetweenTagAndCertificate(long tagId, long giftCertificateId);
}
