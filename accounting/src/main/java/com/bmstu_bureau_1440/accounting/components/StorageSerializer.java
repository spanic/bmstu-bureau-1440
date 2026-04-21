package com.bmstu_bureau_1440.accounting.components;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.FileType;

import java.nio.file.Path;

public interface StorageSerializer {
    FileType getFileType();

    Storage deserialize(Path path) throws Exception;

    void serialize(Storage object, Path path) throws Exception;
}
