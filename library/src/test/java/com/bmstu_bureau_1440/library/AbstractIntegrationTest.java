package com.bmstu_bureau_1440.library;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    static final PostgreSQLContainer<?> PG_CONTAINER = new PostgreSQLContainer<>("postgres:18-alpine")
            .withDatabaseName("library_test")
            .withUsername("test")
            .withPassword("test");

    static {
        PG_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PG_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", PG_CONTAINER::getUsername);
        registry.add("spring.datasource.password", PG_CONTAINER::getPassword);
    }
}
