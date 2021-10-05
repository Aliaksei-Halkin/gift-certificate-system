package com.epam.esm.dao.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Aliaksei Halkin
 */
@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(rs.getLong(ColumnName.CERTIFICATE_ID));
        certificate.setName(rs.getString(ColumnName.NAME));
        certificate.setDescription(rs.getString(ColumnName.DESCRIPTION));
        certificate.setPrice(rs.getBigDecimal(ColumnName.PRICE));
        certificate.setDuration(rs.getInt(ColumnName.DURATION));
        certificate.setCreateDate(rs.getTimestamp(ColumnName.CREATE_DATE).toLocalDateTime());
        certificate.setUpdateDate(rs.getTimestamp(ColumnName.LAST_UPDATE_DATE).toLocalDateTime());
        return certificate;
    }
}
