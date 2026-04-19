package com.bmstu_bureau_1440.accounting.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
public class Category {

    @Getter
    private final String id = UUID.randomUUID().toString();

    @Getter
    @Setter
    private OperationType type;

    @Getter
    @Setter
    private String name;

}
