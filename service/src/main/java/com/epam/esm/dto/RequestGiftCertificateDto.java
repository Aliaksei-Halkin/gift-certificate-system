package com.epam.esm.dto;

import com.epam.esm.exception.ExceptionPropertyKey;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
public class RequestGiftCertificateDto {
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_NAME)
    @Pattern(regexp = "[а-яА-Я\\w\\s\\d\\.,?!]{1,250}",
            message = ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_NAME)
    private String name;
    @NotBlank(message = ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_DESCRIPTION)
    @Pattern(regexp = "[а-яА-Я\\w\\s\\d\\.,?!]{1,250}",
            message = ExceptionPropertyKey.INCORRECT_GIFT_CERTIFICATE_DESCRIPTION)
    private String description;
    @DecimalMin(value = "0.01", message = ExceptionPropertyKey.INCORRECT_MIN_PRICE)
    @DecimalMax(value = "1000000", message = ExceptionPropertyKey.INCORRECT_MAX_PRICE)
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;
    @Min(value = 1, message = ExceptionPropertyKey.INCORRECT_MIN_DURATION)
    @Max(value = 30, message = ExceptionPropertyKey.INCORRECT_MAX_DURATION)
    private int duration;
    @Valid
    private Set<TagDto> tags;
}