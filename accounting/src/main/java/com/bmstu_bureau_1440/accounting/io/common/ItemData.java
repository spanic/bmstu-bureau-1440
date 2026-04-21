package com.bmstu_bureau_1440.accounting.io.common;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class ItemData {

    @Getter
    @NonNull
    private final String title;

    @Getter
    private final String key;

    @Getter
    @Setter
    @NonNull
    private Status status;

    public ItemData(String title, String key, Status status) {
        this.title = title;
        this.key = key;
        this.status = status;
    }

    public ItemData(String title, String key) {
        this.title = title;
        this.key = key;
        this.status = Status.NOT_SELECTED;
    }

    public ItemData(String title) {
        this(title, StringUtils.EMPTY, Status.NOT_SELECTED);
    }

    public enum Status {
        SELECTED,
        NOT_SELECTED
    }

}
