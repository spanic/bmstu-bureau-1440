package com.bmstu_bureau_1440.accounting.components;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.FileType;
import com.bmstu_bureau_1440.accounting.models.Operation;
import com.bmstu_bureau_1440.accounting.utils.CheckIfReadable;
import com.bmstu_bureau_1440.accounting.utils.CheckIfWritable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class CsvStorageSerializer implements StorageSerializer {

    private final CsvSerializer csvSerializer;

    private static final String OPERATIONS_FILENAME = "operations.csv";

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }

    @Override
    @CheckIfReadable(filenames = {OPERATIONS_FILENAME})
    public Storage deserialize(@NonNull Path path) throws Exception {
        var operations = csvSerializer.deserialize(
                path.resolve(OPERATIONS_FILENAME),
                Operation.class
        );

        return new Storage(operations, null, null); // TODO: replace it by the actual result of deserialization
    }

    @Override
    @CheckIfWritable
    public void serialize(@NonNull Storage object, @NonNull Path path) throws Exception {
        csvSerializer.serialize(
                object.getOperations(),
                Files.createDirectories(path).resolve(OPERATIONS_FILENAME)
        );
    }
}
