package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.QueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Aliaksei Halkin
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    public ResponseEntity<GiftCertificateDto> addGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto addedGiftCertificateDto = giftCertificateService.addGiftCertificate(giftCertificateDto);
        return new ResponseEntity<>(addedGiftCertificateDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/tags")
    public ResponseEntity<Set<TagDto>> addTagToGiftCertificate(@PathVariable("id") long giftCertificateId,
                                                               @RequestBody TagDto tagDto) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.addTagToGiftCertificate(giftCertificateId, tagDto);
        return new ResponseEntity<>(giftCertificateDto.getTags(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> findGiftCertificateById(@PathVariable("id") long id) {
        GiftCertificateDto giftCertificate = giftCertificateService.findGiftCertificateById(id);
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<Set<TagDto>> findGiftCertificateTags(@PathVariable("id") long certificateId) {
        GiftCertificateDto giftCertificate = giftCertificateService.findGiftCertificateById(certificateId);
        Set<TagDto> tags = giftCertificate.getTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificatesByParameters
            (@RequestParam(value = "tagName", required = false) String tagName,
             @RequestParam(value = "certificateName", required = false) String certificateName,
             @RequestParam(value = "certificateDescription", required = false) String certificateDescription,
             @RequestParam(value = "order", required = false) String order,
             @RequestParam(value = "direction", required = false) String direction) {
        QueryParameter queryParameter = new QueryParameter(tagName, certificateName, certificateDescription, order, direction);
        List<GiftCertificateDto> giftCertificates = giftCertificateService.findGiftCertificatesByParameters(queryParameter);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@PathVariable("id") long giftCertificateId,
                                                                    @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto updatedGiftCertificateDto = giftCertificateService
                .updateGiftCertificate(giftCertificateId, giftCertificateDto);
        return new ResponseEntity<>(updatedGiftCertificateDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGiftCertificateById(@PathVariable("id") long id) {
        giftCertificateService.deleteGiftCertificateById(id);
    }

}
