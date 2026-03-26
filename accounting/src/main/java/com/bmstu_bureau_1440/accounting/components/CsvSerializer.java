package com.bmstu_bureau_1440.accounting.components;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// TODO: think about replacing OpenCSV used here by Jackson

@Component
public class CsvSerializer {

    /**
     * Deserializes a CSV file into a list of objects of the specified class type.
     *
     * @param path  the path to the CSV file to read from; must not be null
     * @param clazz the class type of the objects to deserialize into
     * @return a list of deserialized objects
     * @throws Exception if an error occurs during reading or parsing
     */
    public <T> List<T> deserialize(@NonNull Path path, Class<T> clazz) throws Exception {
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return new CsvToBeanBuilder<T>(reader).withType(clazz).build().parse();
        }
    }

    /**
     * Serializes a list of objects into a CSV file at the specified path.
     *
     * @param objects the list of objects to serialize; must not be null
     * @param path    the path to the CSV file to write to; must not be null
     * @throws Exception if an error occurs during writing or serialization
     */
    public <T> void serialize(List<T> objects, @NonNull Path path) throws Exception {
        try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            StatefulBeanToCsv<Object> beanToCsv = new StatefulBeanToCsvBuilder<>(writer).build();
            beanToCsv.write(List.of(objects.toArray())); // Workaround for strange OpenCSV behavior
        }
    }
}
