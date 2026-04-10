package com.bmstu_bureau_1440.accounting.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@EqualsAndHashCode
@AllArgsConstructor
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
