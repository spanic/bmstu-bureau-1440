package com.bmstu_bureau_1440.library.converters;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import com.bmstu_bureau_1440.library.models.Genre;

@ReadingConverter
public class GenreReadingConverter implements Converter<PGobject, Genre> {

    @Override
    public Genre convert(PGobject source) {
        return Genre.valueOf(source.getValue());
    }

}
