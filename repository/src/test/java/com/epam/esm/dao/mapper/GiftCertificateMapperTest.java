package com.epam.esm.dao.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Aliaksei Halkin
 */
class GiftCertificateMapperTest {
    private GiftCertificateMapper mapper = new GiftCertificateMapper();
    private ResultSet resultSet = mock(ResultSet.class);

    @Test
    void whenMapRowThenShouldReturnGiftCertificate() throws SQLException {
        when(resultSet.getTimestamp(anyString())).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getLong(ColumnName.CERTIFICATE_ID)).thenReturn(1L);
        GiftCertificate actual = mapper.mapRow(resultSet,1);
        GiftCertificate expected = new GiftCertificate();
        expected.setCreatedDate(actual.getCreatedDate());
        expected.setUpdateDate(actual.getUpdateDate());
        expected.setId(actual.getId());
        assertEquals(expected, actual);
    }
    @Test
    void whenMapRowThenShouldThrowException() throws SQLException {
        when(resultSet.getTimestamp(anyString())).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class,() -> mapper.mapRow(resultSet, 1));
    }
}