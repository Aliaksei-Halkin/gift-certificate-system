package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ExceptionPropertyKey;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Aliaksei Halkin
 */
@Service
public class TagServiceImpl implements TagService {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    private final ModelMapper modelMapper;
    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(ModelMapper modelMapper, TagDao tagDao) {
        this.modelMapper = modelMapper;
        this.tagDao = tagDao;
    }

    @Override
    public TagDto addTag(TagDto tagDto) {
        TagValidator.isValidTag(tagDto);
        Tag addedTag = modelMapper.map(tagDto, Tag.class);
        long tagId = tagDao.add(addedTag);
        addedTag.setId(tagId);
        LOGGER.log(Level.INFO, "Tag added: {}", addedTag);
        return modelMapper.map(addedTag, TagDto.class);
    }

    @Override
    public Set<TagDto> findAllTags() {
        List<Tag> tags = tagDao.findAll();
        return tags.stream().map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toSet());
    }

    @Override
    public TagDto findTagById(long tagId) {
        TagValidator.isValidId(tagId);
        Tag tag = retrieveTag(tagId);
        LOGGER.log(Level.INFO, "Found tag by id = {}, tag: {}", tagId, tag);
        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    public void deleteTagById(long tagId) {
        TagValidator.isValidId(tagId);
        tagDao.removeById(tagId);
        LOGGER.log(Level.INFO, "Tag with id = {} deleted", tagId);
    }

    private Tag retrieveTag(long tagId) {
        Optional<Tag> optionalTag = tagDao.findById(tagId);
        return optionalTag.orElseThrow(() -> new ResourceNotFoundException(ExceptionPropertyKey.TAG_WITH_ID_NOT_FOUND,
                tagId));
    }
}
