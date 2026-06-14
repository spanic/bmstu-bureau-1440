package com.bmstu_bureau_1440.library.models;

import com.bmstu_bureau_1440.shared.io.ListOption;

import lombok.Getter;

public enum Genre implements ListOption {

    ADVENTURE("Приключения"),
    CRIME("Криминал"),
    HORROR("Ужасы"),
    MYSTERY("Мистика"),
    FANTASY("Фэнтези"),
    SCIENCE_FICTION("Научная фантастика"),
    NOVEL("Роман"),
    POETRY("Поэзия"),
    BIOGRAPHY("Биография"),
    HISTORY("История"),
    SCIENCE("Наука"),
    TECHNOLOGY("Технологии"),
    ART("Искусство"),
    OTHER("Другое");

    @Getter
    private final String name;

    Genre(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return getName();
    }

}
