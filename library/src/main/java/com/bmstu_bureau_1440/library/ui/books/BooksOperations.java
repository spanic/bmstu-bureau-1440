package com.bmstu_bureau_1440.library.ui.books;

import org.jline.utils.AttributedStyle;

import com.bmstu_bureau_1440.shared.io.IOperation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public enum BooksOperations implements IOperation {

    ADD_BOOK("add_book", "Добавить новую книгу");

    @NonNull
    @Getter
    private final String operation;

    @NonNull
    @Getter
    private final String text;

    @Getter
    private AttributedStyle style;

}
