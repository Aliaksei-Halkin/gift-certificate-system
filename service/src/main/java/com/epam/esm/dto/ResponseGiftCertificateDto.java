package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ResponseGiftCertificateDto extends RepresentationModel<ResponseGiftCertificateDto> {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdateDate;
}