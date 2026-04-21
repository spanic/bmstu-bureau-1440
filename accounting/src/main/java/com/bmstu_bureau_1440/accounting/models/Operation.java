package com.bmstu_bureau_1440.accounting.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class Operation {

    @Getter
    private final String id;

    @Getter
    private final String bankAccountId;

    @Getter
    private final String categoryId;

    @Getter
    private final BigDecimal amount;

    @Getter
    private final LocalDateTime date;

    @Getter
    @Setter
    private String description;

    public Operation(String bankAccountId, String categoryId, BigDecimal amount, String description) {
        this(UUID.randomUUID().toString(), bankAccountId, categoryId, amount, LocalDateTime.now(), description);
    }

    @JsonCreator
    public Operation(
            @JsonProperty("id") String id,
            @JsonProperty("bankAccountId") String bankAccountId,
            @JsonProperty("categoryId") String categoryId,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("date") LocalDateTime date,
            @JsonProperty("description") String description) {
        this.id = id;
        this.bankAccountId = bankAccountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

}
