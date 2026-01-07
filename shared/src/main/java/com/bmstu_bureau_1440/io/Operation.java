package com.bmstu_bureau_1440.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;

@RequiredArgsConstructor
@AllArgsConstructor
public enum Operation implements IOperation {

    EXIT("exit", "Exit", AttributedStyle.BOLD.foreground(AttributedStyle.RED));

    @NonNull
    @Getter
    private final String operation;

    @NonNull
    @Getter
    private final String text;

    @Getter
    private AttributedStyle style;

}
