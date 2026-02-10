package com.bmstu_bureau_1440.accounting.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class Transaction {

    @Getter
    final private String id = UUID.randomUUID().toString();

}
