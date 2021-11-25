package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;

/**
 * The interface represents {@code GiftCertificate} service
 *
 * @author Aliaksei Halkin
 */
public interface GiftCertificateService {
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate);

    GiftCertificate addTagToGiftCertificate(Long giftCertificateId, Tag tag);

    GiftCertificate findGiftCertificateById(Long id);

    List<GiftCertificate> findGiftCertificatesByParameters(Map<String, String> queryParameters);

    void deleteGiftCertificateById(Long id);

    GiftCertificate updateGiftCertificate(Long giftCertificateId, GiftCertificate giftCertificate);

    List<GiftCertificate> findAllCertificates(Map<String, String> queryParameters);

    GiftCertificate updateField(Long id, Map<String, String> field);
}
