package com.bmstu_bureau_1440.library.ui.books;

import org.jline.utils.AttributedStyle;

import com.bmstu_bureau_1440.shared.io.ListOption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public enum BooksOperations implements ListOption {

    ADD_BOOK("add_book", "Добавить новую книгу");

    @NonNull
    @Getter
    private final String key;

    @NonNull
    @Getter
    private final String displayText;

    @Getter
    private AttributedStyle style;

}
