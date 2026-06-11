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

    ADD_BOOK("Добавить новую книгу"),
    VIEW_BOOKS("Посмотреть все книги"),
    FIND_BOOK_BY_TITLE("Найти книгу");

    @NonNull
    @Getter
    private final String name;

    @Getter
    private AttributedStyle style;

    @Override
    public String getKey() {
        return getName();
    }

}
