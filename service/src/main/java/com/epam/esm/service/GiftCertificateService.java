package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateField;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.TagEntity;

import java.util.List;
import java.util.Map;

/**
 * The interface represents {@code GiftCertificate} service
 *
 * @author Aliaksei Halkin
 */
public interface GiftCertificateService {
    GiftCertificateDto addGiftCertificate(GiftCertificateDto giftCertificate);

    GiftCertificateDto addTagToGiftCertificate(Long giftCertificateId, TagDto tag);

    GiftCertificateDto findGiftCertificateById(Long id);

    List<GiftCertificateDto> findGiftCertificatesByParameters(Map<String, String> queryParameters);

    void deleteGiftCertificateById(Long id);

    GiftCertificateDto updateGiftCertificate(Long giftCertificateId, GiftCertificateDto giftCertificate);

    List<GiftCertificateDto> findAllCertificates(Map<String, String> queryParameters);

    GiftCertificateDto updateGiftCertificateField(Long id, GiftCertificateField giftCertificateField);
}
