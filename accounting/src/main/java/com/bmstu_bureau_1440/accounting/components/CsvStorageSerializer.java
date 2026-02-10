package com.bmstu_bureau_1440.accounting.components;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.FileType;
import com.bmstu_bureau_1440.accounting.models.Transaction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class CsvStorageSerializer implements StorageSerializer {

    private final CsvSerializer csvSerializer;

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }

    @Override
    public Storage deserialize(@NonNull Path path) throws Exception {
        var transactions = csvSerializer.deserialize(
                path.resolve("transactions.csv"),
                Transaction.class
        );

        return new Storage(transactions);
    }

    @Override
    public void serialize(@NonNull Storage object, @NonNull Path path) throws Exception {
        csvSerializer.serialize(
                object.getTransactions(),
                Files.createDirectories(path).resolve("transactions.csv")
        );
    }
}
