package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.IdentifierEntity;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.QueryParameterValidator;
import com.epam.esm.validator.TagValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
    private static final String REGEX_PAGE_KEY = "page";
    private static final String REGEX_PER_PAGE_KEY = "per_page";
    private static final long NO_EXIST_ID = -1;
    private final TagDao tagDao;
    private final TagValidator tagValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator, ModelMapper modelMapper) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public TagDto addTag(TagDto tagDto) {
        TagEntity tag = modelMapper.map(tagDto, TagEntity.class);
        tag.setId(null);
        tag.setActive(true);
        long tagId;
        tagValidator.isValidTag(tag);
        tagId = checkOnExist(tag.getName());
        if (tagId < 0) {
            tagId = tagDao.add(tag);
        }
        tag.setId(tagId);
        LOGGER.log(Level.INFO, "Tag added: {}", tag);
        return modelMapper.map(tag, TagDto.class);
    }

    private long checkOnExist(String nameTag) {
        long idTag = NO_EXIST_ID;
        Optional<TagEntity> tag = tagDao.findByName(nameTag);
        if (tag.isPresent() && tag.get().isActive() == true) {
            throw new ValidationException(ExceptionPropertyKey.EXISTING_TAG, nameTag, IdentifierEntity.TAG);
        }
        if (tag.isPresent() && tag.get().isActive() == false) {
            idTag = tag.get().getId();
            tagDao.changeActiveForTag(tag.get().getName());
        }
        return idTag;
    }

    @Override
    public Set<TagDto> findAllTags(Map<String, String> pages) {
        String page = pages.get(REGEX_PAGE_KEY);
        String perPage = pages.get(REGEX_PER_PAGE_KEY);
        QueryParameterValidator.isValidPage(page);
        QueryParameterValidator.isValidPage(perPage);
        int firstPage = Integer.parseInt(page);
        int numberOfRowOnPage = Integer.parseInt(perPage);
        countTotalPages(firstPage, numberOfRowOnPage);
        List<TagEntity> tags = tagDao.findAll(firstPage, numberOfRowOnPage);
        return tags.stream().map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toSet());
    }

    @Override
    public TagDto findTagById(long tagId) {
        tagValidator.isValidId(tagId);
        TagEntity tag = retrieveTag(tagId);
        LOGGER.log(Level.INFO, "Found tag by id = {}, tag: {}", tagId, tag);
        return modelMapper.map(tag,TagDto.class);
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
        TagEntity tag = retrieveTag(tagId);
        if (tag.isActive() == false) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.TAG_WITH_ID_NOT_FOUND,
                    tagId, IdentifierEntity.TAG);
        }
    }

    private TagEntity retrieveTag(long tagId) {
        Optional<TagEntity> optionalTag = tagDao.findById(tagId);
        return optionalTag.orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.TAG_WITH_ID_NOT_FOUND,
                tagId, IdentifierEntity.TAG));
    }

    private void countTotalPages(int page, int perPage) {
        long totalNumbersOfRows = tagDao.countTotalRows(page, perPage);
        long counterPages;
        if (totalNumbersOfRows % perPage == 0) {
            counterPages = totalNumbersOfRows / perPage;
        } else {
            counterPages = totalNumbersOfRows / perPage + 1;
        }
        if (page > counterPages) {
            throw new ResourceNotFoundException(ExceptionPropertyKey.INCORRECT_MAX_PAGE, counterPages,
                    IdentifierEntity.TAG);
        }
    }

}
