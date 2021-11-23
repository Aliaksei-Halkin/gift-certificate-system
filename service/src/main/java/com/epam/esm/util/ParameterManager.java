package com.epam.esm.util;

import java.util.HashMap;
import java.util.Map;


public class ParameterManager {
    private static final String REGEX_TAG_NAME = "tagName";
    private static final String COMMA = ",";
    private static final String PAGE = "page";
    private static final String PER_PAGE = "per_page";
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_PER_PAGE = "10";

    private ParameterManager() {
    }

    public static Map<String, String> giftCertificateQueryParametersProcessing(Map<String, String> queryParameters) {
        Map<String, String> processedParameters = new HashMap<>();
        queryParameters.forEach((key, value) -> {
            if (key.equalsIgnoreCase(REGEX_TAG_NAME)) {
                if (value.contains(COMMA)) {
                    String[] split = value.split(COMMA);
                    int count = 1;
                    for (String tag : split) {
                        processedParameters.put(key + count++, tag.trim());
                    }
                } else {
                    processedParameters.put(key, value.trim());
                }
            }
        });
        queryParameters.remove(REGEX_TAG_NAME);
        createDefaultPageNumber(processedParameters);
        processedParameters.putAll(queryParameters);
        return processedParameters;
    }

    public static Map<String, String> defaultQueryParametersProcessing(Map<String, String> queryParameters) {
        createDefaultPageNumber(queryParameters);
        return queryParameters;
    }

    private static void createDefaultPageNumber(Map<String, String> processedParameters) {
        if (!processedParameters.containsKey(PAGE)) {
            processedParameters.put(PAGE, DEFAULT_PAGE); 
        }
        if (!processedParameters.containsKey(PER_PAGE)) {
            processedParameters.put(PER_PAGE, DEFAULT_PER_PAGE);
        }
    }

}