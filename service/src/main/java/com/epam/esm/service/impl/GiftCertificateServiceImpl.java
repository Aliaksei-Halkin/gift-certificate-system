package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateField;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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


    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      GiftCertificateValidator giftCertificateValidator, TagValidator tagValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
    }

    /**
     * The method represents adding a new {@link GiftCertificate }to the database
     *
     * @param giftCertificate new {@link GiftCertificate }
     * @return updated and added {@link GiftCertificate }
     */
    @Override
    @Transactional
    public GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) {
        giftCertificateValidator.isValidGiftCertificate(giftCertificate);
        Optional<GiftCertificate> optionalCertificate = giftCertificateValidator.ifExistName(giftCertificate.getName());
        if (optionalCertificate.isPresent()) {
            GiftCertificate updatedCertificate = optionalCertificate.get();
            updatedCertificate.setActive(true);
            giftCertificateDao.update(updatedCertificate);
            return updatedCertificate;
        }
        giftCertificate.setId(null);
        giftCertificate.setActive(true);
        if (giftCertificate.getTags() != null) {
            giftCertificate.getTags().forEach(tagValidator::isValidTag);
            updateTagsInCertificate(giftCertificate);
        }
        giftCertificateDao.add(giftCertificate);
        LOGGER.info("Gift certificate added: " + giftCertificate);
        return giftCertificate;
    }

    /**
     * The method update tags in the database and update tags in entity with correct values.
     *
     * @param giftCertificate the GiftCertificate
     */
    private void updateTagsInCertificate(GiftCertificate giftCertificate) {
        Set<Tag> tags = giftCertificate.getTags();
        Set<Tag> updatedTags = new HashSet<>();
        for (Tag tag : tags) {
            if (tag.isActive()) {
                Optional<Tag> optionalTag = tagDao.findByName(tag.getName());
                if (optionalTag.isPresent()) {
                    Tag receivedTag = optionalTag.get();
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

    private Tag findTag(Tag tag) {
        Optional<Tag> tagByName = tagDao.findByName(tag.getName());
        Tag movableTag = new Tag();
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
     * The method represents adding the {@link Tag} to the {@link GiftCertificate }
     *
     * @param giftCertificateId id of gift certificate
     * @param tag               {@link Tag}
     * @return {@link GiftCertificate } with all tags
     */
    @Override
    @Transactional
    public GiftCertificate addTagToGiftCertificate(Long giftCertificateId, Tag tag) {
        giftCertificateValidator.isValidId(giftCertificateId);
        tagValidator.isValidTag(tag);
        GiftCertificate giftCertificate = checkAndGetGiftCertificate(giftCertificateId);
        Tag updatedTag = findTag(tag);
        giftCertificate.getTags().add(updatedTag);
        GiftCertificate updatedGiftCertificate = giftCertificateDao.update(giftCertificate);
        LOGGER.info("Tag added to gift certificate: " + tag);
        return updatedGiftCertificate;
    }

    /**
     * The method represents getting the gift certificate from the database
     *
     * @param id unique id of gift certificate
     * @return iftCertificate
     */
    private GiftCertificate checkAndGetGiftCertificate(Long id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(id);
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
    public GiftCertificate findGiftCertificateById(Long id) {
        giftCertificateValidator.isValidId(id);
        GiftCertificate giftCertificate = checkAndGetGiftCertificate(id);
        LOGGER.log(Level.INFO, "Found gift certificate by id: ", giftCertificate);
        return giftCertificate;
    }

    /**
     * The method represents finding a gift certificate by query parameters
     *
     * @param queryParameters query parameters
     * @return GiftCertificates
     */
    @Override
    public List<GiftCertificate> findGiftCertificatesByParameters(Map<String, String> queryParameters) {
        Map<String, String> processedQueryParameters = ParameterManager
                .giftCertificateQueryParametersProcessing(queryParameters);
        QueryParameterValidator.isValidGiftCertificateQueryParameters(processedQueryParameters);
        LOGGER.debug("Query parameter: {}", processedQueryParameters);
        countTotalPages(processedQueryParameters);
        return giftCertificateDao.findCertificatesByQueryParameters(processedQueryParameters);
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
        GiftCertificate giftCertificate = checkCertificateOnDoubleDelete(id);
        giftCertificateDao.deactivate(giftCertificate);
        LOGGER.log(Level.INFO, "The certificate with id = {} deleted", id);
    }

    /**
     * The method represents updating
     *
     * @param giftCertificateId the unique id of the gift certificate
     * @param giftCertificate   giftCertificate with new data
     * @return updatedGiftCertificate
     */
    @Override
    @Transactional
    public GiftCertificate updateGiftCertificate(Long giftCertificateId, GiftCertificate giftCertificate) {
        giftCertificateValidator.isValidId(giftCertificateId);
        GiftCertificate giftCertificateVerified = checkAndGetGiftCertificate(giftCertificateId);
        updateFields(giftCertificate, giftCertificateVerified);
        if (giftCertificate.getTags() != null) {
            updateTagsInCertificate(giftCertificate);
        }
        giftCertificateVerified.setTags(giftCertificate.getTags());
        giftCertificateDao.update(giftCertificateVerified);
        return giftCertificateVerified;
    }

    /**
     * Update fields
     *
     * @param receivedGiftCertificate certificate with new fields
     * @param updatedGiftCertificate  certificate from database with old fields
     */
    private void updateFields(GiftCertificate receivedGiftCertificate, GiftCertificate updatedGiftCertificate) {
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

    private GiftCertificate checkCertificateOnDoubleDelete(long id) {
        GiftCertificate giftCertificate = findGiftCertificateById(id);
        if (!giftCertificate.isActive()) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.GIFT_CERTIFICATE_WITH_ID_NOT_FOUND, id,
                    IdentifierEntity.CERTIFICATE);
        }
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> findAllCertificates(Map<String, String> queryParameters) {
        QueryParameterValidator.isValidPage(queryParameters.get(PAGE));
        QueryParameterValidator.isValidPage(queryParameters.get(PER_PAGE));
        countTotalPages(queryParameters);
        return giftCertificateDao.findAll(queryParameters);
    }

    @Override
    @Transactional
    public GiftCertificate updateGiftCertificateField(Long id, GiftCertificateField giftCertificateField) {
        giftCertificateValidator.isValidId(id);
        giftCertificateValidator.isValidField(giftCertificateField);
        GiftCertificate giftCertificate = checkAndGetGiftCertificate(id);
        updateField(giftCertificateField, giftCertificate);
        GiftCertificate updatedCertificate = giftCertificateDao.update(giftCertificate);
        LOGGER.info("Gift certificate with id = {} updated", id);
        return updatedCertificate;
    }

    private void updateField(GiftCertificateField giftCertificateField, GiftCertificate updatedGiftCertificate) {
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
