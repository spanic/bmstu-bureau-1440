package com.bmstu_bureau_1440.library.ui;

import org.jline.utils.AttributedStyle;

import com.bmstu_bureau_1440.shared.io.IOperation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public enum MainOperations implements IOperation {

    LIST_BOOKS_OPERATIONS("list_books_operations", "Книги");

    @NonNull
    @Getter
    private final String operation;

    @NonNull
    @Getter
    private final String text;

    @Getter
    private AttributedStyle style;

}
