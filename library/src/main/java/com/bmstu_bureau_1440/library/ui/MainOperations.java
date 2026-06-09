package com.bmstu_bureau_1440.library.ui;

import org.jline.utils.AttributedStyle;

import com.bmstu_bureau_1440.shared.io.ListOption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public enum MainOperations implements ListOption {

    LIST_BOOKS_OPERATIONS("list_books_operations", "Книги");

    @NonNull
    @Getter
    private final String key;

    @NonNull
    @Getter
    private final String displayText;

    @Getter
    private AttributedStyle style;

}
