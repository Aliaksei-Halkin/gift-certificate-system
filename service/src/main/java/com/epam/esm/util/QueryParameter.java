package com.epam.esm.util;

import java.util.Objects;

/**
 * @author Aliaksei Halkin
 */
public class QueryParameter {
    private String tagName;
    private String certificateName;
    private String certificateDescription;
    private String order;
    private String direction;

    public QueryParameter() {
    }

    public QueryParameter(String tagName, String certificateName, String certificateDescription,
                          String order, String direction) {
        this.tagName = tagName;
        this.certificateName = certificateName;
        this.certificateDescription = certificateDescription;
        this.order = order;
        this.direction = direction;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public void setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryParameter that = (QueryParameter) o;
        return Objects.equals(tagName, that.tagName) && Objects.equals(certificateName, that.certificateName) && Objects.equals(certificateDescription, that.certificateDescription) && Objects.equals(order, that.order) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, certificateName, certificateDescription, order, direction);
    }

    @Override
    public String toString() {
        return "QueryParameter{" +
                "tagName='" + tagName + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", certificateDescription='" + certificateDescription + '\'' +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
