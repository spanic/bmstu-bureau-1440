package com.bmstu_bureau_1440.accounting;

import com.bmstu_bureau_1440.accounting.models.Transaction;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Storage {

    @Getter
    @Setter
    private List<Transaction> transactions;

    public Storage(@NonNull List<Transaction> transactions) {
        this.transactions = transactions.isEmpty() ? new ArrayList<>() : transactions;
    }

}
