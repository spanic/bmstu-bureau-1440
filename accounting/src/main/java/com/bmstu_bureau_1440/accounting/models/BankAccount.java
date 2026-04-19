package com.bmstu_bureau_1440.accounting.models;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
public class BankAccount {

    @Getter
    private final String id = UUID.randomUUID().toString();

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private BigDecimal balance;

}
