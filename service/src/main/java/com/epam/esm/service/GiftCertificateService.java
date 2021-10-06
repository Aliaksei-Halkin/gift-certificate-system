package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.util.QueryParameter;

import java.util.List;

/**
 * @author Aliaksei Halkin
 */
public interface GiftCertificateService {
    GiftCertificateDto addGiftCertificate(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto addTagToGiftCertificate(Long giftCertificateId, TagDto tagDto);

    GiftCertificateDto findGiftCertificateById(Long id);

    List<GiftCertificateDto> findGiftCertificatesByParameters(QueryParameter queryParameter);

    void deleteGiftCertificateById(Long id);

    GiftCertificateDto updateGiftCertificate(Long giftCertificateId, GiftCertificateDto giftCertificateDto);
}
