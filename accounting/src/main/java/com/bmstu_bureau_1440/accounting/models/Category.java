package com.bmstu_bureau_1440.accounting.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class Category {

    @Getter
    private final String id;

    @Getter
    @Setter
    private OperationType type;

    @Getter
    @Setter
    private String name;

    public Category(OperationType type, String name) {
        this(UUID.randomUUID().toString(), type, name);
    }

    @JsonCreator
    public Category(
            @JsonProperty("id") String id,
            @JsonProperty("type") OperationType type,
            @JsonProperty("name") String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

}
