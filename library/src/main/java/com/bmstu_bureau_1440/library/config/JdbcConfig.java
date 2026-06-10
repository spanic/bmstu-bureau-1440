package com.bmstu_bureau_1440.library.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import com.bmstu_bureau_1440.library.converters.GenreReadingConverter;
import com.bmstu_bureau_1440.library.converters.GenreWritingConverter;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Override
    protected List<?> userConverters() {
        return List.of(
                new GenreReadingConverter(),
                new GenreWritingConverter());
    }

}