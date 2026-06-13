package com.bmstu_bureau_1440.library.ui.operations;

import org.jline.utils.AttributedStyle;

import com.bmstu_bureau_1440.shared.io.ListOption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public enum Operations implements ListOption {

    PROVIDE_BOOK("Выдать книгу"),
    RETURN_BOOK("Вернуть книгу"),
    VIEW_BOOKS_PROVIDED_TO_CLIENT("Посмотреть все книги, выданные читателю");

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
