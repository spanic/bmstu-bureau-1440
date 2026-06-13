package com.bmstu_bureau_1440.library.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bmstu_bureau_1440.library.converters.GenericListOptionReadingConverter;
import com.bmstu_bureau_1440.library.converters.GenericListOptionWritingConverter;
import com.bmstu_bureau_1440.library.models.Genre;
import com.bmstu_bureau_1440.library.models.OperationType;

@Configuration
@EnableJdbcAuditing
@EnableTransactionManagement
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Override
    protected List<?> userConverters() {
        return List.of(
                new GenericListOptionReadingConverter<>(Genre.class),
                new GenericListOptionReadingConverter<>(OperationType.class),
                new GenericListOptionWritingConverter<>(Genre.class, "book_genre"),
                new GenericListOptionWritingConverter<>(OperationType.class, "operation_type"));
    }

}