package com.bmstu_bureau_1440.accounting.io.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum InputFields {

    ACCOUNT_NAME("name", "account-name-input"),
    ACCOUNT_BALANCE("balance", "account-balance-input"),

    CATEGORY_NAME("name", "category-name-input"),
    CATEGORY_TYPE("type", "category-type-select"),

    OPERATION_ACCOUNT("operation-account", "operation-account-select"),
    OPERATION_CATEGORY("operation-category", " operation-category-select"),
    OPERATION_AMOUNT("amount", "operation-amount-input"),
    OPERATION_DESCRIPTION("description", "operation-description-input");

    @Getter
    private final String fieldName;

    @Getter
    private final String fieldId;

}
