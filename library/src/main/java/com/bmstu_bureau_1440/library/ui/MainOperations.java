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

    LIST_BOOKS_OPERATIONS("Книги"),
    LIST_CLIENTS_OPERATIONS("Читатели"),
    OPERATIONS("Операции"),
    STATISTICS("Статистика");

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
