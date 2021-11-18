package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Locale;

/**
 * @author Aliaksei Halkin
 */
@SpringBootApplication
@ConfigurationPropertiesScan("com.epam.esm")
public class GiftCertificateApp {
    public static void main(String[] args) {
        SpringApplication.run(GiftCertificateApp.class, args);
    }
}
