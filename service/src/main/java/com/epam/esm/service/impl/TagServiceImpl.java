package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class represents methods for work   with dao level, validation
 *
 * @author Aliaksei Halkin
 */
@Service
public class TagServiceImpl implements TagService {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    private static final long NO_EXIST_ID = -1;
    private final TagDao tagDao;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    @Transactional
    @Override
    public Tag addTag(Tag tag) {
        tag.setId(null);
        tag.setActive(true);
        long tagId;
        tagValidator.isValidTag(tag);
        tagId = ifExist(tag.getName());
        if (tagId < 0) {
            tagId = tagDao.add(tag);
        }
        tag.setId(tagId);
        LOGGER.log(Level.INFO, "Tag added: {}", tag);
        return tag;
    }

    private long ifExist(String nameTag) {
        long idTag = NO_EXIST_ID;
        Optional<Tag> tag = tagDao.findTagByName(nameTag);
        if (tag.isPresent() && tag.get().isActive() == true) {
            throw new ValidationException(ExceptionPropertyKey.EXISTING_TAG, nameTag);
        }
        if (tag.isPresent() && tag.get().isActive() == false) {
            idTag = tag.get().getId();
            tagDao.changeActiveForTag(tag.get().getName());
        }
        return idTag;
    }

    @Override
    public Set<Tag> findAllTags() {
        List<Tag> tags = tagDao.findAll();
        return tags.stream().collect(Collectors.toSet());
    }

    @Override
    public Tag findTagById(long tagId) {
        tagValidator.isValidId(tagId);
        Tag tag = retrieveTag(tagId);
        LOGGER.log(Level.INFO, "Found tag by id = {}, tag: {}", tagId, tag);
        return tag;
    }

    @Transactional
    @Override
    public void deleteTagById(long tagId) {
        tagValidator.isValidId(tagId);
        checkTagOnDoubleDelete(tagId);
        tagDao.removeById(tagId);
        LOGGER.log(Level.INFO, "Tag with id = {} deleted", tagId);
    }

    private void checkTagOnDoubleDelete(long tagId) {
        Tag tag = retrieveTag(tagId);
        if (tag.isActive() == false) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.TAG_WITH_ID_NOT_FOUND,
                    tagId);
        }
    }

    private Tag retrieveTag(long tagId) {
        Optional<Tag> optionalTag = tagDao.findById(tagId);
        return optionalTag.orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.TAG_WITH_ID_NOT_FOUND,
                tagId));
    }

}
