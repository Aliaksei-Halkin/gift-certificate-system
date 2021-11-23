package com.epam.esm.util;

/**
 * The fields of QueryParameter class represents dats for QueryParameterManager
 *
 * @author Aliaksei Halkin
 */
public class QueryParameter {
    private String tagName;
    private String certificateName;
    private String certificateDescription;
    private String order;
    private String page;
    private String per_page;

    public QueryParameter() {
    }

    public QueryParameter(String tagName, String certificateName, String certificateDescription, String order) {
        this.tagName = tagName;
        this.certificateName = certificateName;
        this.certificateDescription = certificateDescription;
        this.order = order;
    }

    public QueryParameter(String tagName, String certificateName, String certificateDescription, String order,
                          String page, String per_page) {
        this.tagName = tagName;
        this.certificateName = certificateName;
        this.certificateDescription = certificateDescription;
        this.order = order;
        this.page = page;
        this.per_page = per_page;
    }

    public String getTagName() {
        return tagName;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public String getOrder() {
        return order;
    }

    public String getPage() {
        return page;
    }

    public String getPer_page() {
        return per_page;
    }

    @Override
    public String toString() {
        return "QueryParameter{" +
                "tagsName='" + tagName + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", certificateDescription='" + certificateDescription + '\'' +
                ", order='" + order + '\'' +
                ", page=" + page +
                ", per_page=" + per_page +
                '}';
    }
}
