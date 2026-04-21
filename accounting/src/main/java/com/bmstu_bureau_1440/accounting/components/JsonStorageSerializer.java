package com.bmstu_bureau_1440.accounting.components;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.FileType;
import com.bmstu_bureau_1440.accounting.utils.CheckIfReadable;
import com.bmstu_bureau_1440.accounting.utils.CheckIfWritable;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class JsonStorageSerializer implements StorageSerializer {

    ObjectMapper mapper = new ObjectMapper();

    private static final String JSON_FILENAME = "accounting.json";

    @Override
    public FileType getFileType() {
        return FileType.JSON;
    }

    @Override
    @CheckIfReadable(filenames = {JSON_FILENAME})
    public Storage deserialize(Path path) throws Exception {
        return mapper.readValue(path.resolve(JSON_FILENAME), Storage.class);
    }

    @Override
    @CheckIfWritable
    public void serialize(Storage object, Path path) throws Exception {
        mapper.writerWithDefaultPrettyPrinter().writeValue(
                Files.createDirectories(path).resolve(JSON_FILENAME), object
        );
    }
}
