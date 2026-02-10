package com.bmstu_bureau_1440.accounting.components;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.FileType;
import com.bmstu_bureau_1440.accounting.models.Transaction;
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

    private static final String TRANSACTIONS_FILENAME = "transactions.csv";

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }

    @Override
    @CheckIfReadable(filenames = {TRANSACTIONS_FILENAME})
    public Storage deserialize(@NonNull Path path) throws Exception {
        var transactions = csvSerializer.deserialize(
                path.resolve(TRANSACTIONS_FILENAME),
                Transaction.class
        );

        return new Storage(transactions);
    }

    @Override
    @CheckIfWritable
    public void serialize(@NonNull Storage object, @NonNull Path path) throws Exception {
        csvSerializer.serialize(
                object.getTransactions(),
                Files.createDirectories(path).resolve(TRANSACTIONS_FILENAME)
        );
    }
}
