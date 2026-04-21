package com.bmstu_bureau_1440.accounting.components;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.FileType;
import com.bmstu_bureau_1440.accounting.utils.CheckIfReadable;
import com.bmstu_bureau_1440.accounting.utils.CheckIfWritable;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class YamlStorageSerializer implements StorageSerializer {

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    private static final String YAML_FILENAME = "accounting.yaml";

    @Override
    public FileType getFileType() {
        return FileType.YAML;
    }

    @Override
    @CheckIfReadable(filenames = {YAML_FILENAME})
    public Storage deserialize(Path path) throws Exception {
        return mapper.readValue(path.resolve(YAML_FILENAME), Storage.class);
    }

    @Override
    @CheckIfWritable
    public void serialize(Storage object, Path path) throws Exception {
        mapper.writeValue(
                Files.createDirectories(path).resolve(YAML_FILENAME), object
        );
    }
}
