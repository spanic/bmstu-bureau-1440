package com.bmstu_bureau_1440.library.converters;

import java.sql.JDBCType;
import java.sql.SQLException;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.mapping.JdbcValue;

import com.bmstu_bureau_1440.library.models.Genre;

@WritingConverter
public class GenreWritingConverter implements Converter<Genre, JdbcValue> {

    private static final String PG_TYPE = "book_genre";

    @Override
    public JdbcValue convert(Genre source) {
        var pgObject = new PGobject();
        pgObject.setType(PG_TYPE);
        try {
            pgObject.setValue(source.name());
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to convert genre to PGobject", e);
        }
        return JdbcValue.of(pgObject, JDBCType.OTHER);
    }

}
