package com.epam.esm.dao.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The {@code GiftCertificateMapper} class build the {@code GiftCertificate} entity from resultset database
 *
 * @author Aliaksei Halkin
 */
@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    /**
     * This method   map each row of data in the ResultSet.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return {@link GiftCertificate} entity with full fields
     */
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(rs.getLong(ColumnName.CERTIFICATE_ID));
        certificate.setName(rs.getString(ColumnName.NAME));
        certificate.setDescription(rs.getString(ColumnName.DESCRIPTION));
        certificate.setPrice(rs.getBigDecimal(ColumnName.PRICE));
        certificate.setDuration(rs.getInt(ColumnName.DURATION));
        certificate.setCreatedDate(rs.getTimestamp(ColumnName.CREATE_DATE).toLocalDateTime());
        certificate.setUpdateDate(rs.getTimestamp(ColumnName.LAST_UPDATE_DATE).toLocalDateTime());
        certificate.setActive(rs.getBoolean(ColumnName.ACTIVE));
        return certificate;
    }
}
