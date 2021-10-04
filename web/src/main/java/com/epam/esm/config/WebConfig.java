package com.epam.esm.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Aliaksei Halkin
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
public class WebConfig {
    private final String EXCEPTION_MESSAGE_FILE_PATH = "exception_messages";
    private final String ENCODING = "UTF-8";

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(EXCEPTION_MESSAGE_FILE_PATH);
        messageSource.setDefaultEncoding(ENCODING);
        return messageSource;
    }
}
