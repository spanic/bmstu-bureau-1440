package com.bmstu_bureau_1440.accounting;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.models.Operation;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Component
public class Storage {

    @Getter
    @Setter
    private List<Operation> operations;

    @Getter
    @Setter
    private List<Category> categories;

    @Getter
    @Setter
    private List<BankAccount> accounts;

    public Storage(@JsonProperty("operations") List<Operation> operations,
            @JsonProperty("accounts") List<BankAccount> accounts,
            @JsonProperty("categories") List<Category> categories) {
        this.operations = ListUtils.emptyIfNull(operations);
        this.accounts = ListUtils.emptyIfNull(accounts);
        this.categories = ListUtils.emptyIfNull(categories);
    }

}
