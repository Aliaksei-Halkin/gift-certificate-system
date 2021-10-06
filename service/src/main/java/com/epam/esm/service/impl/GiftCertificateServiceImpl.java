package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.QueryParameter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aliaksei Halkin
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Override
    public GiftCertificateDto addGiftCertificate(GiftCertificateDto giftCertificateDto) {
        return null;
    }

    @Override
    public GiftCertificateDto addTagToGiftCertificate(Long giftCertificateId, TagDto tagDto) {
        return null;
    }

    @Override
    public GiftCertificateDto findGiftCertificateById(Long id) {
        return null;
    }

    @Override
    public List<GiftCertificateDto> findGiftCertificatesByParameters(QueryParameter queryParameter) {
        return null;
    }

    @Override
    public void deleteGiftCertificateById(Long id) {

    }

    @Override
    public GiftCertificateDto updateGiftCertificate(Long giftCertificateId, GiftCertificateDto giftCertificateDto) {
        return null;
    }
}
