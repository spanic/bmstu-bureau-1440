package com.bmstu_bureau_1440.banking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Customer {

    @NonNull
    @Getter
    final private String id = UUID.randomUUID().toString();
    @NonNull
    @Getter
    private String name;

}
