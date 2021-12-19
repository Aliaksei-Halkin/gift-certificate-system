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
    private static final String ORDER = "order";

    private ParameterManager() {
    }

    public static Map<String, String> giftCertificateQueryParametersProcessing(Map<String, String> queryParameters) {
        Map<String, String> processedParameters = new HashMap<>();
        changeOrderParamToLowerCase(processedParameters);
        queryParameters.forEach((key, value) -> {
            if (key.equalsIgnoreCase(REGEX_TAG_NAME)) {
                if (value.contains(COMMA)) {
                    String[] split = value.split(COMMA);
                    int count = 1;
                    for (String tag : split) {
                        processedParameters.put(key + count++, tag.trim());
                    }
                } else {
                    if (!value.trim().isEmpty()) {
                        processedParameters.put(key, value);
                    }
                }
            }
        });
        queryParameters.remove(REGEX_TAG_NAME);
        createDefaultPageNumber(processedParameters);
        processedParameters.putAll(queryParameters);
        return processedParameters;
    }

    private static void changeOrderParamToLowerCase(Map<String, String> processedParameters) {
        if (processedParameters.containsKey(ORDER)) {
            String valueOrderInLowerCase = processedParameters.get(ORDER).toLowerCase();
            processedParameters.put(ORDER, valueOrderInLowerCase);
        }
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