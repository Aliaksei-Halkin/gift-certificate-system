package com.epam.esm.util;

import com.epam.esm.service.GiftCertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The QueryParameterManager represents constructions for the query. It's addition for the main query.
 *
 * @author Aliaksei Halkin
 */
public class QueryParameterBuilder {
    private static final String WHERE = "WHERE ";
    private static final String AND = "AND ";
    private static final String OR = "OR ";
    private static final String PERCENT_AND = "%' AND ";
    private static final String PERCENT_OR = "%' OR ";
    private static final String PERCENT = "%' ";
    private static final String BLANK = "";
    private static final String DESCRIPTION = "description";
    private static final String DESC = "desc";
    private static final String TAG_NAME_KEY = "tagName([1-9]\\d*)?";
    private static final String FIRST_TAG_NAME = "tagName1";
    private static final String TAG_NAME = "tagName";
    private static final String NAME = "name";
    private static final String ORDER = "order";
    private static final char MINUS = '-';
    private static final String PAGE = "page";
    private static final String PER_PAGE = "per_page";
    private static final String JOIN_TAGS = "JOIN g.tags t ";
    private static final String GIFT_CERTIFICATES_NAME_LIKE = "g.name LIKE '%";
    private static final String TAG_NAME_LIKE = "t.name LIKE '%";
    private static final String GIFT_CERTIFICATES_DESCRIPTION_LIKE = "g.description LIKE '%";
    private static final String ORDER_BY_GIFT_CERTIFICATE_NAME = "ORDER BY g.name ";
    private static final String ORDER_BY_GIFT_CERTIFICATE_DESCRIPTION = "ORDER BY g.description ";
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);


    public static String createQueryForCertificates(Map<String, String> queryParameters) {
        removePageParameters(queryParameters);
        Map<String, String> params = new HashMap<>(queryParameters);
        StringBuilder query = new StringBuilder();
        if (queryParameters.containsKey(FIRST_TAG_NAME) || queryParameters.containsKey(TAG_NAME)) {
            query.append(JOIN_TAGS).append(WHERE);
            params.forEach((key, value) -> {
                if (key.matches(TAG_NAME_KEY)) {
                    query.append(TAG_NAME_LIKE).append(value).append(PERCENT_OR);
                    queryParameters.remove(key, value);
                }
            });
            if (queryParameters.isEmpty()) {
                query.replace(query.length() - OR.length(), query.length(), BLANK);
            } else {
                query.replace(query.length() - OR.length(), query.length(), AND);
            }
        } else if (!queryParameters.isEmpty()) {
            query.append(WHERE);
        }
        if (queryParameters.containsKey(NAME)) {
            query.append(GIFT_CERTIFICATES_NAME_LIKE).append(queryParameters.get(NAME)).append(PERCENT_AND);
        }
        if (queryParameters.containsKey(DESCRIPTION)) {
            query.append(GIFT_CERTIFICATES_DESCRIPTION_LIKE).append(queryParameters.get(DESCRIPTION)).append(PERCENT_AND);
        }
        if (query.toString().endsWith(AND)) {
            query.replace(query.length() - AND.length(), query.length(), BLANK);
        }
        if (query.toString().endsWith(WHERE)) {
            query.replace(query.length() - WHERE.length(), query.length(), BLANK);
        }
        if (queryParameters.containsKey(ORDER)) {
            String order = queryParameters.get(ORDER);
            char direction = order.charAt(0);
            if (order.contains(NAME)) {
                query.append(ORDER_BY_GIFT_CERTIFICATE_NAME);
            }
            if (order.contains(DESCRIPTION)) {
                query.append(ORDER_BY_GIFT_CERTIFICATE_DESCRIPTION);
            }
            if (direction == MINUS) {
                query.append(DESC);
            }
        }
        LOGGER.debug("Created query: {}", query);
        return query.toString();
    }

    private static Map<String, String> removePageParameters(Map<String, String> queryParameters) {
        queryParameters.remove(PAGE);
        queryParameters.remove(PER_PAGE);
        return queryParameters;
    }
}
