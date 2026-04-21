package com.bmstu_bureau_1440.accounting.components;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.NonNull;
import tools.jackson.dataformat.csv.CsvMapper;
import tools.jackson.dataformat.csv.CsvSchema;

@Component
public class CsvSerializer {

    private final CsvMapper mapper = new CsvMapper();

    /**
     * Deserializes a CSV file into a list of objects of the specified class type.
     * Assumes the first row is a header mapping columns to bean properties.
     *
     * @param path  the path to the CSV file to read from; must not be null
     * @param clazz the class type of the objects to deserialize into
     * @return a list of deserialized objects
     * @throws Exception if an error occurs during reading or parsing
     */
    public <T> List<T> deserialize(@NonNull Path path, @NonNull Class<T> clazz) throws Exception {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader();
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return mapper.readerFor(clazz)
                    .with(schema)
                    .<T>readValues(reader)
                    .readAll();
        }
    }

    /**
     * Serializes a list of objects into a CSV file at the specified path.
     * The column layout and header are derived from {@code clazz}, so an empty
     * list still produces a header-only file.
     *
     * @param objects the list of objects to serialize; must not be null
     * @param clazz   the element class used to derive the CSV schema; must not be
     *                null
     * @param path    the path to the CSV file to write to; must not be null
     * @throws Exception if an error occurs during writing or serialization
     */
    public <T> void serialize(@NonNull List<T> objects, @NonNull Class<T> clazz, @NonNull Path path) throws Exception {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader();
        try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            mapper.writer(schema).writeValue(writer, objects);
        }
    }
}
