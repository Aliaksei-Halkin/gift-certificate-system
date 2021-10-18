package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.QueryParameter;

import java.util.List;

/**
 * The interface represents {@code GiftCertificate} service
 *
 * @author Aliaksei Halkin
 */
public interface GiftCertificateService {
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate);

    GiftCertificate addTagToGiftCertificate(Long giftCertificateId, Tag tag);

    GiftCertificate findGiftCertificateById(Long id);

    List<GiftCertificate> findGiftCertificatesByParameters(QueryParameter queryParameter);

    void deleteGiftCertificateById(Long id);

    GiftCertificate updateGiftCertificate(Long giftCertificateId, GiftCertificate giftCertificate);
}
