package com.bmstu_bureau_1440.banking;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Customer {

    @NonNull
    @Getter
    private final String id = UUID.randomUUID().toString();
    @NonNull
    @Getter
    private String name;

}
