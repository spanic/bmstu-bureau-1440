package com.bmstu_bureau_1440.accounting.utils;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Aspect
@Component
public class PathAccessCheckAspect {

    @Before("@annotation(ensureWritable) && args(.., path)")
    public void validateIfWritable(CheckIfWritable checkIfWritable, Path path) throws Throwable {
        if (!Files.isWritable(path.getParent()))
            throw new IllegalArgumentException("Cannot write into the specified location: " + path);
    }

    @Before("@annotation(ensureReadable) && args(.., path)")
    public void validateIfReadable(CheckIfReadable checkIfReadable, Path path) throws Throwable {
        final String[] filenames = checkIfReadable.filenames();

        for (String filename : filenames) {
            var pathToFile = path.resolve(filename);

            if (!Files.exists(pathToFile))
                throw new IllegalArgumentException("File doesn't exist: " + pathToFile);

            if (!Files.isReadable(pathToFile))
                throw new IllegalArgumentException("File is not readable: " + pathToFile);
        }
    }

}
