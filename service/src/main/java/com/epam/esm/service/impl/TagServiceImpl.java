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
    private final TagDao tagDao;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    @Override
    public Tag addTag(Tag tag) {
        tagValidator.isValidTag(tag);
        checkTagIsPresentInDatabase(tag.getName());
        long tagId = tagDao.add(tag);
        tag.setId(tagId);
        LOGGER.log(Level.INFO, "Tag added: {}", tag);
        return tag;
    }

    private void checkTagIsPresentInDatabase(String nameTag) {
        List<String> namesOfCertificates = tagDao.findAll().stream().map(c -> c.getName()).collect(Collectors.toList());
        if (namesOfCertificates.contains(nameTag)) {
            throw new ValidationException(ExceptionPropertyKey.EXISTING_TAG, nameTag);
        }
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

    @Override
    public void deleteTagById(long tagId) {
        tagValidator.isValidId(tagId);
        findTagById(tagId);
        tagDao.removeById(tagId);
        LOGGER.log(Level.INFO, "Tag with id = {} deleted", tagId);
    }

    private Tag retrieveTag(long tagId) {
        Optional<Tag> optionalTag = tagDao.findById(tagId);
        return optionalTag.orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.TAG_WITH_ID_NOT_FOUND,
                tagId));
    }

}
