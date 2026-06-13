package com.bmstu_bureau_1440.library.converters;

import java.sql.JDBCType;
import java.sql.SQLException;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.mapping.JdbcValue;

import com.bmstu_bureau_1440.shared.io.ListOption;

@WritingConverter
public class GenericListOptionWritingConverter<E extends Enum<E> & ListOption>
        implements Converter<E, JdbcValue>, ConditionalConverter {

    private final Class<E> enumType;

    private final String pgType;

    public GenericListOptionWritingConverter(Class<E> enumType, String pgType) {
        this.enumType = enumType;
        this.pgType = pgType;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return enumType.isAssignableFrom(sourceType.getType());
    }

    @Override
    public JdbcValue convert(E source) {
        var pgObject = new PGobject();
        pgObject.setType(pgType);
        try {
            pgObject.setValue(source.name());
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to convert genre to PGobject", e);
        }
        return JdbcValue.of(pgObject, JDBCType.OTHER);
    }

}
