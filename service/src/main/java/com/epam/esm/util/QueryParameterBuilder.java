package com.epam.esm.util;

import com.epam.esm.service.GiftCertificateService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The QueryParameterManager represents constructions for the query. It's addition for the main query.
 *
 * @author Aliaksei Halkin
 */
public class QueryParameterBuilder {
    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    private static final String JOIN_CERTIFICATES_HAS_TAGS_AND_TAGS =
            "JOIN certificates_has_tags ON gift_certificates.certificateId = certificates_has_tags.certificateId " +
                    "JOIN tags ON tags.tagId = certificates_has_tags.tagId ";
    private static final String WHERE = "WHERE ";
    private static final String AND = "AND ";
    private static final String PERCENT = "%' AND ";
    private static final String GIFT_CERTIFICATE_NAME = "gift_certificates.name LIKE '%";
    private static final String TAG_NAME = "tags.tagName LIKE '%";
    private static final String GIFT_CERTIFICATE_DESCRIPTION = "gift_certificates.description LIKE '%";
    private static final String ORDER_BY_GIFT_CERTIFICATE_NAME = "ORDER BY gift_certificates.name ";
    private static final String ORDER_BY_GIFT_CERTIFICATE_DESCRIPTION = "ORDER BY gift_certificates.description ";
    private static final String BLANK = "";
    private static final String DESCRIPTION = "description";
    private static final String ASC = "asc";
    private static final String DESC = "desc";

    public static String createQuery(QueryParameter queryParameter) {
        StringBuilder query = isParametersExist(queryParameter);
        if (queryParameter.getTagName() != null
                && !queryParameter.getTagName().isEmpty()) {
            query.append(TAG_NAME).append(queryParameter.getTagName()).append(PERCENT);
        }
        if (queryParameter.getCertificateName() != null
                && !queryParameter.getCertificateName().isEmpty()) {
            query.append(GIFT_CERTIFICATE_NAME)
                    .append(queryParameter.getCertificateName()).append(PERCENT);
        }
        if (queryParameter.getCertificateDescription() != null
                && !queryParameter.getCertificateDescription().isEmpty()) {
            query.append(GIFT_CERTIFICATE_DESCRIPTION)
                    .append(queryParameter.getCertificateDescription()).append(PERCENT);
        }
        if (query.toString().endsWith(AND)) {
            query.replace(query.length() - AND.length(), query.length(), BLANK);
        }
        if (queryParameter.getOrder() != null && !queryParameter.getOrder().isEmpty()) {
            if (queryParameter.getOrder().equals(DESCRIPTION)) {
                query.append(ORDER_BY_GIFT_CERTIFICATE_DESCRIPTION);
            } else {
                query.append(ORDER_BY_GIFT_CERTIFICATE_NAME);
            }
        }
        if (queryParameter.getDirection() != null && !queryParameter.getDirection().isEmpty()) {
            if (queryParameter.getDirection().equals(DESC)) {
                query.append(DESC.toUpperCase());
            } else {
                query.append(ASC.toUpperCase());
            }
        }
        LOGGER.log(Level.DEBUG, "Created query: {}", query);
        return query.toString();
    }

    private static StringBuilder isParametersExist(QueryParameter queryParameter) {
        StringBuilder query = new StringBuilder();
        if ((queryParameter.getTagName() == null || queryParameter.getTagName().isEmpty())) {
            if ((queryParameter.getCertificateName() == null || queryParameter.getCertificateName().isEmpty())
                    && (queryParameter.getCertificateDescription() == null || queryParameter.getCertificateDescription().isEmpty())) {
                return query;
            } else {
                return query.append(WHERE);
            }
        } else {
            return query.append(JOIN_CERTIFICATES_HAS_TAGS_AND_TAGS + WHERE);
        }
    }
}
