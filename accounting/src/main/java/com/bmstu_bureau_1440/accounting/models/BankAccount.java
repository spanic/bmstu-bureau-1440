package com.bmstu_bureau_1440.accounting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
public class BankAccount {

    @Getter
    final private String id = UUID.randomUUID().toString();

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private BigDecimal balance;

}
