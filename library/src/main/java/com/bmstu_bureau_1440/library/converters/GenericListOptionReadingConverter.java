package com.bmstu_bureau_1440.library.converters;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import com.bmstu_bureau_1440.shared.io.ListOption;

@ReadingConverter
public class GenericListOptionReadingConverter<E extends Enum<E> & ListOption>
        implements Converter<PGobject, E>, ConditionalConverter {

    private final Class<E> enumType;

    public GenericListOptionReadingConverter(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return enumType.isAssignableFrom(targetType.getType());
    }

    @Override
    public E convert(PGobject source) {
        return Enum.valueOf(enumType, source.getValue());
    }

}
