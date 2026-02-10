package com.bmstu_bureau_1440.accounting.components;

import java.nio.file.Path;
import java.util.List;

public interface Serializer {
    <T> List<T> deserialize(Path path, Class<T> clazz) throws Exception;

    <T> void serialize(List<T> object, Path path) throws Exception;
}
