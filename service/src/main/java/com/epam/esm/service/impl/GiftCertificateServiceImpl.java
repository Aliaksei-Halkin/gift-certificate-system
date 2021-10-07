package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.QueryParameter;
import com.epam.esm.util.QueryParameterManager;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.QueryParameterValidator;
import com.epam.esm.validator.TagValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Aliaksei Halkin
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final ModelMapper modelMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao, ModelMapper modelMapper) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public GiftCertificateDto addGiftCertificate(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.setCreatedDate(LocalDateTime.now());
        giftCertificateDto.setUpdateDate(LocalDateTime.now());
        GiftCertificateValidator.isValidGiftCertificate(giftCertificateDto);
        if (giftCertificateDto.getTags() != null) {
            giftCertificateDto.getTags().forEach(TagValidator::isValidTag);
        }
        GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        long certificateId = giftCertificateDao.add(giftCertificate);
        giftCertificate.setId(certificateId);
        if (giftCertificate.getTags() != null) {
            giftCertificate.getTags().forEach(tag -> checkAndAddRelationBetweenTagAndGiftCertificate(giftCertificate.getId(), tag));
        }
        LOGGER.info("Gift certificate added: " + giftCertificate);
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    private void checkAndAddRelationBetweenTagAndGiftCertificate(Long giftCertificateId, Tag tag) {
        Tag processedTag;
        if (checkIfTagAlreadyExist(tag)) {
            LOGGER.debug("Tag already exist: " + tag);
            processedTag = tagDao.findTagByName(tag.getName())
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.TAG_WITH_NAME_NOT_FOUND,
                            tag.getName()));
        } else {
            LOGGER.debug("New tag: " + tag);
            long tagId = tagDao.add(tag);
            processedTag = tag;
            processedTag.setId(tagId);
        }
        tag.setId(processedTag.getId());
        giftCertificateDao.addRelationBetweenTagAndGiftCertificate(processedTag.getId(), giftCertificateId);

    }

    private boolean checkIfTagAlreadyExist(Tag tag) {
        return tagDao.findAll().contains(tag);
    }

    @Override
    @Transactional
    public GiftCertificateDto addTagToGiftCertificate(Long giftCertificateId, TagDto tagDto) {
        GiftCertificateValidator.isValidId(giftCertificateId);
        TagValidator.isValidTag(tagDto);
        GiftCertificate giftCertificate = checkAndGetGiftCertificate(giftCertificateId);
        giftCertificate.setUpdateDate(LocalDateTime.now());
        Tag tag = modelMapper.map(tagDto, Tag.class);
        checkAndAddRelationBetweenTagAndGiftCertificate(giftCertificateId, tag);
        GiftCertificate updatedGiftCertificate = giftCertificateDao.update(giftCertificate);
        Set<Tag> giftCertificateTags = giftCertificateDao.findGiftCertificateTags(giftCertificateId);
        updatedGiftCertificate.setTags(giftCertificateTags);
        LOGGER.info("Tag added to gift certificate: " + tag);
        return modelMapper.map(updatedGiftCertificate, GiftCertificateDto.class);
    }

    private GiftCertificate checkAndGetGiftCertificate(Long id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(id);
        return giftCertificateOptional
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.GIFT_CERTIFICATE_WITH_ID_NOT_FOUND, id));
    }

    @Override
    public GiftCertificateDto findGiftCertificateById(Long id) {
        GiftCertificateValidator.isValidId(id);
        GiftCertificate giftCertificate = checkAndGetGiftCertificate(id);
        giftCertificate.setTags(giftCertificateDao.findGiftCertificateTags(id));
        LOGGER.log(Level.INFO, "Found gift certificate by id: ", giftCertificate);
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    @Override
    public List<GiftCertificateDto> findGiftCertificatesByParameters(QueryParameter queryParameter) {
        QueryParameterValidator.isValidQueryParameters(queryParameter);
        String query = QueryParameterManager.createQuery(queryParameter);
        LOGGER.log(Level.DEBUG, "Query parameter:  ", queryParameter);
        List<GiftCertificate> giftCertificates = giftCertificateDao.findCertificatesByQueryParameters(query);
        for (GiftCertificate certificate : giftCertificates) {
            Set<Tag> giftCertificateTags = giftCertificateDao.findGiftCertificateTags(certificate.getId());
            certificate.setTags(giftCertificateTags);
        }
        return giftCertificates.stream()
                .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGiftCertificateById(Long id) {
        GiftCertificateValidator.isValidId(id);
        giftCertificateDao.removeById(id);
    }

    @Override
    public GiftCertificateDto updateGiftCertificate(Long giftCertificateId, GiftCertificateDto giftCertificateDto) {
        GiftCertificateValidator.isValidId(giftCertificateId);
        GiftCertificate giftCertificate = checkAndGetGiftCertificate(giftCertificateId);
        updateFields(giftCertificateDto, giftCertificate);
        giftCertificate.setUpdateDate(LocalDateTime.now());
        GiftCertificate updatedGiftCertificate = giftCertificateDao.update(giftCertificate);
        giftCertificate.getTags().forEach(tag -> checkAndAddRelationBetweenTagAndGiftCertificate(giftCertificateId, tag));
        Set<Tag> changedTags = giftCertificateDao.findGiftCertificateTags(giftCertificateId);
        updatedGiftCertificate.setTags(changedTags);
        LOGGER.log(Level.INFO, "Gift certificate with id = {} updated", giftCertificateId);
        return modelMapper.map(updatedGiftCertificate, GiftCertificateDto.class);
    }

    private void updateFields(GiftCertificateDto receivedGiftCertificate, GiftCertificate updatedGiftCertificate) {
        if (receivedGiftCertificate.getName() != null && !receivedGiftCertificate.getName().isEmpty()) {
            updatedGiftCertificate.setName(receivedGiftCertificate.getName());
        }
        if (receivedGiftCertificate.getDescription() != null && !receivedGiftCertificate.getDescription().isEmpty()) {
            updatedGiftCertificate.setDescription(receivedGiftCertificate.getDescription());
        }
        if (receivedGiftCertificate.getPrice() != null) {
            updatedGiftCertificate.setPrice(receivedGiftCertificate.getPrice());
        }
        if (receivedGiftCertificate.getDuration() > 0) {
            updatedGiftCertificate.setDuration(receivedGiftCertificate.getDuration());
        }
        GiftCertificateValidator.isValidGiftCertificate(modelMapper.map(updatedGiftCertificate, GiftCertificateDto.class));
        if (receivedGiftCertificate.getTags() != null) {
            receivedGiftCertificate.getTags().forEach(TagValidator::isValidTag);
        }
        Set<Tag> giftCertificateTags = giftCertificateDao.findGiftCertificateTags(updatedGiftCertificate.getId());
        Set<Tag> addedTags = new HashSet<>();
        if (receivedGiftCertificate.getTags() != null) {
            receivedGiftCertificate.getTags().stream().map(tagDto -> modelMapper.map(tagDto, Tag.class)).forEach(tag -> {
                if (!giftCertificateTags.contains(tag)) {
                    addedTags.add(tag);
                }
            });
        }
        updatedGiftCertificate.setTags(addedTags);
        LOGGER.log(Level.DEBUG, "Updated gift certificate: {}", updatedGiftCertificate);
    }
}
