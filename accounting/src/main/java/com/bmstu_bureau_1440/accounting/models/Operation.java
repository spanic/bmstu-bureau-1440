package com.bmstu_bureau_1440.accounting.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class Operation {

    @Getter
    private final String id = UUID.randomUUID().toString();

    @Getter
    private final String bankAccountId;

    @Getter
    private final String categoryId;

    @Getter
    private final BigDecimal amount;

    @Getter
    private final LocalDateTime date = LocalDateTime.now();

    @Getter
    @Setter
    private String description;

}
