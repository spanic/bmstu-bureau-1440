package com.bmstu_bureau_1440.shared.io;

import java.util.Arrays;
import java.util.stream.Stream;

import org.jline.utils.AttributedStyle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public enum Operation implements ListOption {

    EXIT("exit", "Exit", AttributedStyle.BOLD.foreground(AttributedStyle.RED));

    public static ListOption[] withExit(ListOption[] operations) {
        return Stream.concat(Arrays.stream(operations), Stream.of(Operation.EXIT)).toArray(ListOption[]::new);
    }

    @NonNull
    @Getter
    private final String key;

    @NonNull
    @Getter
    private final String name;

    @Getter
    private AttributedStyle style;

}
