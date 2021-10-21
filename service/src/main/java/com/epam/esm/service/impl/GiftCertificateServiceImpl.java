package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.QueryParameter;
import com.epam.esm.util.QueryParameterManager;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.QueryParameterValidator;
import com.epam.esm.validator.TagValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The class represents service methods witch work with dao level, validation
 *
 * @author Aliaksei Halkin
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
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
        giftCertificate.setCreatedDate(LocalDateTime.now());
        giftCertificate.setUpdateDate(LocalDateTime.now());
        giftCertificateValidator.isValidGiftCertificate(giftCertificate);
        giftCertificateValidator.checkNameInDataBase(giftCertificate.getName());
        if (giftCertificate.getTags() != null) {
            giftCertificate.getTags().forEach(tagValidator::isValidTag);
        }
        long certificateId = giftCertificateDao.add(giftCertificate);
        giftCertificate.setId(certificateId);
        if (giftCertificate.getTags() != null) {
            giftCertificate.getTags().forEach(tag -> attachedTag(giftCertificate.getId(), tag));
        }
        giftCertificate.setTags(giftCertificateDao.findGiftCertificateTags(certificateId));
        LOGGER.info("Gift certificate added: " + giftCertificate);
        return giftCertificate;
    }

    /**
     * Initially, we check if the tag exists in the database. If it is not, we add it to the database.
     * If the tag exists in the database, but is not added to the certificate, we add it to the certificate
     *
     * @param giftCertificateId {@code Long} unique identifier of gift certificate
     * @param tag               {@code Tag} attaching Tag
     */
    private void attachedTag(Long giftCertificateId, Tag tag) {
        boolean isPresentTag = tagDao.findTagByName(tag.getName()).isPresent();
        if (isPresentTag == false) {
            tagDao.add(tag);
        }
        Tag movableTag = tagDao.findTagByName(tag.getName()).get();
        if (isPresentTagInGiftCertificate(giftCertificateId, movableTag) == false) {
            giftCertificateDao.attachedTag(movableTag.getId(), giftCertificateId);
        }
    }

    /**
     * Check is present tag in gift certificate
     *
     * @param giftCertificateId id of gift certificate
     * @param tag               {@link Tag}
     * @return true-if present or else
     */
    private boolean isPresentTagInGiftCertificate(Long giftCertificateId, Tag tag) {
        return giftCertificateDao.findGiftCertificateTags(giftCertificateId).contains(tag);
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
        giftCertificate.setUpdateDate(LocalDateTime.now());
        attachedTag(giftCertificateId, tag);
        GiftCertificate updatedGiftCertificate = giftCertificateDao.update(giftCertificate);
        Set<Tag> giftCertificateTags = giftCertificateDao.findGiftCertificateTags(giftCertificateId);
        updatedGiftCertificate.setTags(giftCertificateTags);
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
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.GIFT_CERTIFICATE_WITH_ID_NOT_FOUND, id));
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
        giftCertificate.setTags(giftCertificateDao.findGiftCertificateTags(id));
        LOGGER.log(Level.INFO, "Found gift certificate by id: ", giftCertificate);
        return giftCertificate;
    }

    /**
     * The method represents finding a gift certificate by query parameters
     *
     * @param queryParameter query parameters
     * @return GiftCertificates
     */
    @Override
    public List<GiftCertificate> findGiftCertificatesByParameters(QueryParameter queryParameter) {
        QueryParameterValidator.isValidQueryParameters(queryParameter);
        String query = QueryParameterManager.createQuery(queryParameter);
        LOGGER.log(Level.DEBUG, "Query parameter:  ", queryParameter);
        List<GiftCertificate> giftCertificates = giftCertificateDao.findCertificatesByQueryParameters(query);
        for (GiftCertificate certificate : giftCertificates) {
            Set<Tag> giftCertificateTags = giftCertificateDao.findGiftCertificateTags(certificate.getId());
            certificate.setTags(giftCertificateTags);
        }
        return giftCertificates;
    }

    /**
     * The method represents delete GiftCertificate by Id
     *
     * @param id id unique id of gift certificate
     */
    @Override
    public void deleteGiftCertificateById(Long id) {
        giftCertificateValidator.isValidId(id);
        giftCertificateDao.removeById(id);
    }

    /**
     * The method represents updating
     *
     * @param giftCertificateId the unique id of the gift certificate
     * @param giftCertificate   giftCertificate with new data
     * @return updatedGiftCertificate
     */
    @Override
    public GiftCertificate updateGiftCertificate(Long giftCertificateId, GiftCertificate giftCertificate) {
        giftCertificateValidator.isValidId(giftCertificateId);
        GiftCertificate giftCertificateVerified = checkAndGetGiftCertificate(giftCertificateId);
        updateFields(giftCertificate, giftCertificateVerified);
        giftCertificate.setUpdateDate(LocalDateTime.now());
        GiftCertificate updatedGiftCertificate = giftCertificateDao.update(giftCertificateVerified);
        giftCertificate.getTags().forEach(tag -> attachedTag(giftCertificateId, tag));
        Set<Tag> changedTags = giftCertificateDao.findGiftCertificateTags(giftCertificateId);
        updatedGiftCertificate.setTags(changedTags);
        LOGGER.log(Level.INFO, "Gift certificate with id = {} updated", giftCertificateId);
        return updatedGiftCertificate;
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
        giftCertificateValidator.isValidGiftCertificate(updatedGiftCertificate);
        if (receivedGiftCertificate.getTags() != null) {
            receivedGiftCertificate.getTags().forEach(tagValidator::isValidTag);
        }
        Set<Tag> giftCertificateTags = giftCertificateDao.findGiftCertificateTags(updatedGiftCertificate.getId());
        Set<Tag> addedTags = new HashSet<>();
        if (receivedGiftCertificate.getTags() != null) {
            receivedGiftCertificate.getTags().stream().forEach(tag -> {
                if (!giftCertificateTags.contains(tag)) {
                    addedTags.add(tag);
                }
            });
        }
        updatedGiftCertificate.setTags(addedTags);
        LOGGER.log(Level.DEBUG, "Updated gift certificate: {}", updatedGiftCertificate);
    }
}
