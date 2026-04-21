package com.bmstu_bureau_1440.accounting.components;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.models.FileType;
import com.bmstu_bureau_1440.accounting.models.Operation;
import com.bmstu_bureau_1440.accounting.utils.CheckIfReadable;
import com.bmstu_bureau_1440.accounting.utils.CheckIfWritable;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsvStorageSerializer implements StorageSerializer {

    private final CsvSerializer csvSerializer;

    private static final String OPERATIONS_FILENAME = "operations.csv";
    private static final String ACCOUNTS_FILENAME = "accounts.csv";
    private static final String CATEGORIES_FILENAME = "categories.csv";

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }

    @Override
    @CheckIfReadable(filenames = { OPERATIONS_FILENAME, ACCOUNTS_FILENAME, CATEGORIES_FILENAME })
    public Storage deserialize(@NonNull Path path) throws Exception {
        var operations = csvSerializer.deserialize(
                path.resolve(OPERATIONS_FILENAME),
                Operation.class);

        var accounts = csvSerializer.deserialize(
                path.resolve(ACCOUNTS_FILENAME),
                BankAccount.class);

        var categories = csvSerializer.deserialize(
                path.resolve(CATEGORIES_FILENAME),
                Category.class);

        return new Storage(operations, accounts, categories);
    }

    @Override
    @CheckIfWritable
    public void serialize(@NonNull Storage object, @NonNull Path path) throws Exception {
        csvSerializer.serialize(
                object.getOperations(),
                Operation.class,
                Files.createDirectories(path).resolve(OPERATIONS_FILENAME));

        csvSerializer.serialize(
                object.getAccounts(),
                BankAccount.class,
                Files.createDirectories(path).resolve(ACCOUNTS_FILENAME));

        csvSerializer.serialize(
                object.getCategories(),
                Category.class,
                Files.createDirectories(path).resolve(CATEGORIES_FILENAME));
    }

}
