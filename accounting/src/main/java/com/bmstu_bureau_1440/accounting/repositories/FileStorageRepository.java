package com.bmstu_bureau_1440.accounting.repositories;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.components.StorageSerializer;
import com.bmstu_bureau_1440.accounting.models.FileType;

@Repository
public class FileStorageRepository {

    final Path exportPath = Paths.get("accounting", "export");

    private final Map<FileType, StorageSerializer> serializers;

    private final Storage storage;

    public FileStorageRepository(List<StorageSerializer> serializers, Storage storage) {
        this.serializers = serializers.stream()
                .collect(Collectors.toMap(StorageSerializer::getFileType, Function.identity()));
        this.storage = storage;
    }

    public void exportToFile(FileType fileType) {
        try {
            StorageSerializer serializer = serializers.get(fileType);
            serializer.serialize(storage, exportPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void importFromFile(FileType fileType) {
        try {
            StorageSerializer serializer = serializers.get(fileType);
            var restoredStorage = serializer.deserialize(exportPath);
            storage.setOperations(restoredStorage.getOperations());
            storage.setAccounts(restoredStorage.getAccounts());
            storage.setCategories(restoredStorage.getCategories());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
