package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import static com.epam.esm.column.ColumnNameTag.*;

/**
 * The {@code TagMapper} class build the {@code GiftCertificate} entity from resultset database
 *
 * @author Aliaksei Halkin
 */
@Component
public class TagMapper implements RowMapper<Tag> {
    /**
     * This method   map each row of data in the ResultSet.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return {@link Tag} entity with full fields
     */
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setActive(rs.getBoolean(ACTIVE.getName()));
        tag.setId(rs.getLong(TAG_ID.getName()));
        tag.setName(rs.getString(TAG_NAME.getName()));
        return tag;
    }
}
