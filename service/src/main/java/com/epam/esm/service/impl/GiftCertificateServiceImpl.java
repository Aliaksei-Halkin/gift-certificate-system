package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateField;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.ParameterManager;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class represents service methods witch work with dao level, validation
 *
 * @author Aliaksei Halkin
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    private static final int INITIAL_PAGE_VALUE = 1;
    private static final String PAGE = "page";
    private static final String PER_PAGE = "per_page";
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final GiftCertificateValidator giftCertificateValidator;
    private final TagValidator tagValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      GiftCertificateValidator giftCertificateValidator, TagValidator tagValidator, ModelMapper modelMapper) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.modelMapper = modelMapper;
    }

    /**
     * The method represents adding a new {@link GiftCertificateEntity }to the database
     *
     * @param giftCertificateDto new {@link GiftCertificateDto }
     * @return updated and added {@link GiftCertificateDto }
     */
    @Override
    @Transactional
    public GiftCertificateDto addGiftCertificate(GiftCertificateDto giftCertificateDto) {
        GiftCertificateEntity giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificateEntity.class);
        giftCertificateValidator.isValidGiftCertificate(giftCertificate);
        Optional<GiftCertificateEntity> optionalCertificate = giftCertificateValidator.ifExistName(giftCertificate.getName());
        if (optionalCertificate.isPresent()) {
            GiftCertificateEntity updatedCertificate = optionalCertificate.get();
            updatedCertificate.setActive(true);
            giftCertificateDao.update(updatedCertificate);
            return modelMapper.map(updatedCertificate, GiftCertificateDto.class);
        }
               if (giftCertificate.getTags() != null) {
            giftCertificate.getTags().forEach(tagValidator::isValidTag);
            updateTagsInCertificate(giftCertificate);
        }
        giftCertificateDao.add(giftCertificate);
        LOGGER.info("Gift certificate added: " + giftCertificate);
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    /**
     * The method update tags in the database and update tags in entity with correct values.
     *
     * @param giftCertificate the GiftCertificate
     */
    private void updateTagsInCertificate(GiftCertificateEntity giftCertificate) {
        Set<TagEntity> tags = giftCertificate.getTags();
        Set<TagEntity> updatedTags = new HashSet<>();
        for (TagEntity tag : tags) {
            if (tag.isActive()) {
                Optional<TagEntity> optionalTag = tagDao.findByName(tag.getName());
                if (optionalTag.isPresent()) {
                    TagEntity receivedTag = optionalTag.get();
                    if (!receivedTag.isActive()) {
                        tagDao.changeActiveForTag(receivedTag.getName());
                        receivedTag.setActive(true);
                    }
                    updatedTags.add(receivedTag);
                } else {
                    tag.setId(null);
                    long idAddedTag = tagDao.add(tag);
                    tag.setId(idAddedTag);
                    tag.setActive(true);
                    updatedTags.add(tag);
                }
            }
        }
        giftCertificate.setTags(updatedTags);
    }

    private TagEntity findTag(TagEntity tag) {
        Optional<TagEntity> tagByName = tagDao.findByName(tag.getName());
        TagEntity movableTag = new TagEntity();
        if (!tagByName.isPresent()) {
            tag.setId(null);
            tag.setActive(true);
            long id = tagDao.add(tag);
            movableTag.setId(id);
            movableTag.setName(tag.getName());
        } else {
            movableTag = tagByName.get();
            movableTag.setActive(true);
        }
        return movableTag;
    }

    /**
     * The method represents adding the {@link TagEntity} to the {@link GiftCertificateEntity }
     *
     * @param giftCertificateId id of gift certificate
     * @param tagDto               {@link TagEntity}
     * @return {@link GiftCertificateEntity } with all tags
     */
    @Override
    @Transactional
    public GiftCertificateDto addTagToGiftCertificate(Long giftCertificateId, TagDto tagDto) {
        TagEntity tag = modelMapper.map(tagDto, TagEntity.class);
        giftCertificateValidator.isValidId(giftCertificateId);
        tagValidator.isValidTag(tag);
        GiftCertificateEntity giftCertificate = checkAndGetGiftCertificate(giftCertificateId);
        TagEntity updatedTag = findTag(tag);
        giftCertificate.getTags().add(updatedTag);
        GiftCertificateEntity updatedGiftCertificate = giftCertificateDao.update(giftCertificate);
        LOGGER.info("Tag added to gift certificate: " + tag);
        return modelMapper.map(updatedGiftCertificate, GiftCertificateDto.class);
    }

    /**
     * The method represents getting the gift certificate from the database
     *
     * @param id unique id of gift certificate
     * @return iftCertificate
     */
    private GiftCertificateEntity checkAndGetGiftCertificate(Long id) {
        Optional<GiftCertificateEntity> giftCertificateOptional = giftCertificateDao.findById(id);
        return giftCertificateOptional
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.GIFT_CERTIFICATE_WITH_ID_NOT_FOUND,
                        id, IdentifierEntity.CERTIFICATE));
    }

    /**
     * The method represents finding a gift certificate
     *
     * @param id unique id of gift certificate
     * @return GiftCertificate
     */
    @Override
    public GiftCertificateDto findGiftCertificateById(Long id) {
        giftCertificateValidator.isValidId(id);
        GiftCertificateEntity giftCertificate = checkAndGetGiftCertificate(id);
        LOGGER.log(Level.INFO, "Found gift certificate by id: ", giftCertificate);
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    /**
     * The method represents finding a gift certificate by query parameters
     *
     * @param queryParameters query parameters
     * @return GiftCertificates
     */
    @Override
    public List<GiftCertificateDto> findGiftCertificatesByParameters(Map<String, String> queryParameters) {
        Map<String, String> processedQueryParameters = ParameterManager
                .giftCertificateQueryParametersProcessing(queryParameters);
        QueryParameterValidator.isValidGiftCertificateQueryParameters(processedQueryParameters);
        LOGGER.debug("Query parameter: {}", processedQueryParameters);
        List<GiftCertificateEntity> giftCertificates = giftCertificateDao
                .findCertificatesByQueryParameters(new HashMap<>(processedQueryParameters));
        if (giftCertificates.isEmpty()) {
            throw new ResourceNotFoundException(ExceptionPropertyKey
                    .GIFT_CERTIFICATES_NOT_FOUND, null, IdentifierEntity.CERTIFICATE);
        }
        countTotalPages(processedQueryParameters);
        return giftCertificates
                .stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    /**
     * The method represents delete GiftCertificate by Id
     *
     * @param id id unique id of gift certificate
     */
    @Override
    @Transactional
    public void deleteGiftCertificateById(Long id) {
        giftCertificateValidator.isValidId(id);
        GiftCertificateEntity giftCertificate = checkCertificateOnDoubleDelete(id);
        giftCertificateDao.deactivate(giftCertificate);
        LOGGER.log(Level.INFO, "The certificate with id = {} deleted", id);
    }

    /**
     * The method represents updating
     *
     * @param giftCertificateId the unique id of the gift certificate
     * @param giftCertificateDto   giftCertificate with new data
     * @return updatedGiftCertificate
     */
    @Override
    @Transactional
    public GiftCertificateDto updateGiftCertificate(Long giftCertificateId, GiftCertificateDto giftCertificateDto) {
        GiftCertificateEntity giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificateEntity.class);
        giftCertificateValidator.isValidId(giftCertificateId);
        GiftCertificateEntity giftCertificateVerified = checkAndGetGiftCertificate(giftCertificateId);
        updateFields(giftCertificate, giftCertificateVerified);
        if (giftCertificate.getTags() != null) {
            updateTagsInCertificate(giftCertificate);
        }
        giftCertificateVerified.setTags(giftCertificate.getTags());
        giftCertificateDao.update(giftCertificateVerified);
        return modelMapper.map(giftCertificateVerified, GiftCertificateDto.class);
    }

    /**
     * Update fields
     *
     * @param receivedGiftCertificate certificate with new fields
     * @param updatedGiftCertificate  certificate from database with old fields
     */
    private void updateFields(GiftCertificateEntity receivedGiftCertificate, GiftCertificateEntity updatedGiftCertificate) {
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
        if (receivedGiftCertificate.isActive() || !receivedGiftCertificate.isActive()) {
            updatedGiftCertificate.setActive(receivedGiftCertificate.isActive());
        }
        giftCertificateValidator.isValidGiftCertificate(updatedGiftCertificate);
        if (receivedGiftCertificate.getTags() != null) {
            receivedGiftCertificate.getTags().forEach(tagValidator::isValidTag);
        }
        LOGGER.log(Level.DEBUG, "Updated gift certificate: {}", updatedGiftCertificate);
    }

    private GiftCertificateEntity checkCertificateOnDoubleDelete(long id) {
        GiftCertificateEntity giftCertificate = checkAndGetGiftCertificate(id);
        if (!giftCertificate.isActive()) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.GIFT_CERTIFICATE_WITH_ID_NOT_FOUND, id,
                    IdentifierEntity.CERTIFICATE);
        }
        return giftCertificate;
    }

    @Override
    public List<GiftCertificateDto> findAllCertificates(Map<String, String> queryParameters) {
        QueryParameterValidator.isValidPage(queryParameters.get(PAGE));
        QueryParameterValidator.isValidPage(queryParameters.get(PER_PAGE));
        countTotalPages(queryParameters);
        List<GiftCertificateEntity> giftCertificateEntitys = giftCertificateDao.findAll(queryParameters);
        return giftCertificateEntitys
                .stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GiftCertificateDto updateGiftCertificateField(Long id, GiftCertificateField giftCertificateField) {
        giftCertificateValidator.isValidId(id);
        giftCertificateValidator.isValidField(giftCertificateField);
        GiftCertificateEntity giftCertificate = checkAndGetGiftCertificate(id);
        updateField(giftCertificateField, giftCertificate);
        GiftCertificateEntity updatedCertificate = giftCertificateDao.update(giftCertificate);
        LOGGER.info("Gift certificate with id = {} updated", id);
        return modelMapper.map(updatedCertificate, GiftCertificateDto.class);
    }

    private void updateField(GiftCertificateField giftCertificateField, GiftCertificateEntity updatedGiftCertificate) {
        GiftCertificateField.FieldName fieldName = GiftCertificateField.FieldName.valueOf(giftCertificateField
                .getFieldName().toUpperCase());
        switch (fieldName) {
            case NAME:
                updatedGiftCertificate.setName(giftCertificateField.getFieldValue());
                break;
            case DESCRIPTION:
                updatedGiftCertificate.setDescription(giftCertificateField.getFieldValue());
                break;
            case PRICE:
                updatedGiftCertificate.setPrice(new BigDecimal(giftCertificateField.getFieldValue()));
                break;
            case DURATION:
                updatedGiftCertificate.setDuration(Integer.parseInt(giftCertificateField.getFieldValue()));
                break;
        }
    }

    /**
     * The method  count total page by queryParameters. If the requested page does not exist an error is called
     *
     * @param queryParameters
     */
    private void countTotalPages(Map<String, String> queryParameters) {
        Map<String, String> localQueryParameters = new HashMap<>(queryParameters);
        int page = Integer.parseInt(localQueryParameters.get(PAGE));
        int perPage = Integer.parseInt(localQueryParameters.get(PER_PAGE));
        long totalNumbersOfRows = giftCertificateDao.countTotalRows(localQueryParameters);
        long counterPages = INITIAL_PAGE_VALUE;
        if (totalNumbersOfRows % perPage == 0) {
            counterPages = totalNumbersOfRows / perPage;
        } else {
            counterPages = totalNumbersOfRows / perPage + 1;
        }
        if (page > counterPages) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.INCORRECT_MAX_PAGE, counterPages,
                    IdentifierEntity.CERTIFICATE);
        }
    }
}
