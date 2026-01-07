package com.bmstu_bureau_1440.banking;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@EqualsAndHashCode
@ToString
public class Transaction {

    public static final String TRANSACTION_SUCCESS_MESSAGE = "OK";

    @NonNull
    @Getter
    private TransactionType type;
    @Getter
    private double amount;
    @Getter
    private String fromAccountNumber;
    @Getter
    private String toAccountNumber;
    @Getter
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();
    @Getter
    private boolean success;
    @Getter
    private String message;

}
