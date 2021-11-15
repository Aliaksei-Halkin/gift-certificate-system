package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.QueryParameter;
import com.epam.esm.util.QueryParameterBuilder;
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
        long certificateId = giftCertificateValidator.ifExistName(giftCertificate.getName());
        if (giftCertificate.getTags() != null) {
            giftCertificate.getTags().forEach(tagValidator::isValidTag);
        }
        if (certificateId < 0) {
            certificateId = giftCertificateDao.add(giftCertificate);
        }
        giftCertificate.setId(certificateId);
        giftCertificate.setActive(true);
        final long id = giftCertificate.getId();
        if (giftCertificate.getTags() != null) {
            deleteTagsFromGiftCertificate(id, giftCertificate);
            giftCertificate.getTags().forEach(tag -> attachTag(id, tag));
        }
        giftCertificate.setTags(giftCertificateDao.findGiftCertificateTags(id));
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
    private void attachTag(Long giftCertificateId, Tag tag) {
        Optional<Tag> tagByName = tagDao.findTagByName(tag.getName());
        Tag movableTag = new Tag();
        if (!tagByName.isPresent()) {
            long id = tagDao.add(tag);
            movableTag.setId(id);
            movableTag.setName(tag.getName());
        } else {
            movableTag = tagByName.get();
        }
        if (isPresentTagInGiftCertificate(giftCertificateId, movableTag) == false) {
            giftCertificateDao.attachTag(movableTag.getId(), giftCertificateId);
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
        attachTag(giftCertificateId, tag);
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
//        QueryParameterValidator.isValidQueryParameters(queryParameter);
//      String query = QueryParameterBuilder.createQueryForCertificates(queryParameter);
//        LOGGER.log(Level.DEBUG, "Query parameter:  ", queryParameter);
//        List<GiftCertificate> giftCertificates = giftCertificateDao.findCertificatesByQueryParameters(query);
//        for (GiftCertificate certificate : giftCertificates) {
//            Set<Tag> giftCertificateTags = giftCertificateDao.findGiftCertificateTags(certificate.getId());
//            certificate.setTags(giftCertificateTags);
//        }
//        return giftCertificates;
        return null;
    }
//todo check
    /**
     * The method represents delete GiftCertificate by Id
     *
     * @param id id unique id of gift certificate
     */
    @Override
    public void deleteGiftCertificateById(Long id) {
        giftCertificateValidator.isValidId(id);
        checkCertificateOnDoubleDelete(id);
        giftCertificateDao.removeById(id);
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
    public GiftCertificate updateGiftCertificate(Long giftCertificateId, GiftCertificate giftCertificate) {
        giftCertificateValidator.isValidId(giftCertificateId);
        GiftCertificate giftCertificateVerified = checkAndGetGiftCertificate(giftCertificateId);
        updateFields(giftCertificate, giftCertificateVerified);
        giftCertificateVerified.setUpdateDate(LocalDateTime.now());
        giftCertificateDao.update(giftCertificateVerified);
        Set<Tag> tagsGiftCertificate = deleteTagsFromGiftCertificate(giftCertificateId, giftCertificate);
        tagsGiftCertificate.forEach(tag -> attachTag(giftCertificateId, tag));
        LOGGER.log(Level.INFO, "Gift certificate with id = {} updated", giftCertificateId);
        return findGiftCertificateById(giftCertificateId);
    }

    private Set<Tag> deleteTagsFromGiftCertificate(Long giftCertificateId, GiftCertificate giftCertificate) {
        Set<Tag> tagsGiftCertificate = giftCertificate.getTags();
        Set<Tag> changingTags = giftCertificateDao.findGiftCertificateTags(giftCertificateId);
        Set<String> namesTagsGiftCertificate = tagsGiftCertificate.stream().map(tag -> tag.getName()).collect(Collectors.toSet());
        for (Tag tagIterator : changingTags) {
            if (!namesTagsGiftCertificate.contains(tagIterator.getName())) {
                giftCertificateDao.removeTag(giftCertificateId, tagIterator.getId());
            }
        }
        return tagsGiftCertificate;
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
        if (receivedGiftCertificate.isActive() == true || receivedGiftCertificate.isActive() == false) {
            updatedGiftCertificate.setActive(receivedGiftCertificate.isActive());
        }
        giftCertificateValidator.isValidGiftCertificate(updatedGiftCertificate);
        if (receivedGiftCertificate.getTags() != null) {
            receivedGiftCertificate.getTags().forEach(tagValidator::isValidTag);
        }
        LOGGER.log(Level.DEBUG, "Updated gift certificate: {}", updatedGiftCertificate);
    }

    private void checkCertificateOnDoubleDelete(long id) {
        GiftCertificate giftCertificate = findGiftCertificateById(id);
        if (giftCertificate.isActive() == false) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.GIFT_CERTIFICATE_WITH_ID_NOT_FOUND, id);
        }
    }

    public Map<String, String> toMap(QueryParameter queryParameter) {
        Map<String, String> mapParameter = new HashMap<>();
        String certificateDescription = queryParameter.getCertificateDescription();
        String certificateName = queryParameter.getCertificateName();
        String direction = queryParameter.getDirection();
        String tagName = queryParameter.getTagName();
        String order=queryParameter.getOrder();
                if (!certificateDescription.isEmpty() && certificateDescription != null) {
            mapParameter.put("description", certificateDescription);
        }
        if (!certificateName.isEmpty() && certificateName != null) {
            mapParameter.put("certificateName",certificateName);
        }
        if (!tagName.isEmpty() && tagName != null) {
            mapParameter.put("tagName",tagName);
        }

        if (!order.isEmpty() && order != null) {
            mapParameter.put("order",order.toLowerCase());
        }
        if (!direction.isEmpty() && direction != null) {
            mapParameter.put("direction",direction.toLowerCase());
        }
        return mapParameter;
    }
}
