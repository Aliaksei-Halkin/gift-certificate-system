package com.epam.esm.dao.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.column.ColumnNameGiftCertificate.*;


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
        certificate.setId(rs.getLong(CERTIFICATE_ID.getName()));
        certificate.setName(rs.getString(NAME.getName()));
        certificate.setDescription(rs.getString(DESCRIPTION.getName()));
        certificate.setPrice(rs.getBigDecimal(PRICE.getName()));
        certificate.setDuration(rs.getInt(DURATION.getName()));
        certificate.setCreatedDate(rs.getTimestamp(CREATE_DATE.getName()).toLocalDateTime());
        certificate.setUpdateDate(rs.getTimestamp(LAST_UPDATE_DATE.getName()).toLocalDateTime());
        certificate.setActive(rs.getBoolean(ACTIVE.getName()));
        return certificate;
    }
}
