package com.bmstu_bureau_1440.library.ui.statistics;

import org.jline.utils.AttributedStyle;

import com.bmstu_bureau_1440.shared.io.ListOption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public enum StatisticsOperations implements ListOption {

    SHOW_TRENDING_BOOKS("Показать популярные книги");

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
