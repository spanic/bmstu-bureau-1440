package com.bmstu_bureau_1440.accounting.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@RequiredArgsConstructor
public class Category {

    @Getter
    final private String id = UUID.randomUUID().toString();

    @Getter
    @Setter
    private OperationType type;

    @Getter
    @Setter
    private String name;

}
