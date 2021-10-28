package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.dao.mapper.ColumnNameTag.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Aliaksei Halkin
 */
class TagMapperTest {
    private TagMapper mapper = new TagMapper();
    private ResultSet resultSet = mock(ResultSet.class);

    @Test
    void whenMapRowThenShouldReturnTag() throws SQLException {
        when(resultSet.getLong(TAG_ID.getName())).thenReturn(1L);
        when(resultSet.getString(TAG_NAME.getName())).thenReturn("rest");
        when(resultSet.getBoolean(ACTIVE.getName())).thenReturn(true);
        Tag actual = mapper.mapRow(resultSet, 1);
        Tag expected = new Tag(1L, "rest", true);
        assertEquals(expected, actual);
    }
}