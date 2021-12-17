package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateField;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;

import java.util.List;
import java.util.Map;

/**
 * The interface represents {@code GiftCertificate} service
 *
 * @author Aliaksei Halkin
 */
public interface GiftCertificateService {
    GiftCertificateEntity addGiftCertificate(GiftCertificateEntity giftCertificate);

    GiftCertificateEntity addTagToGiftCertificate(Long giftCertificateId, TagEntity tag);

    GiftCertificateEntity findGiftCertificateById(Long id);

    List<GiftCertificateEntity> findGiftCertificatesByParameters(Map<String, String> queryParameters);

    void deleteGiftCertificateById(Long id);

    GiftCertificateEntity updateGiftCertificate(Long giftCertificateId, GiftCertificateEntity giftCertificate);

    List<GiftCertificateEntity> findAllCertificates(Map<String, String> queryParameters);

    GiftCertificateEntity updateGiftCertificateField(Long id, GiftCertificateField giftCertificateField);
}
