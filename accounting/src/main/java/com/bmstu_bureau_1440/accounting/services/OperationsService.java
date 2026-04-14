package com.bmstu_bureau_1440.accounting.services;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OperationsService {

    private final Storage storage;

    private final AccountsService accountsService;

    public Operation addNewOperation(String accountId, String categoryId, BigDecimal amount, String description) {
        Operation operation = new Operation(accountId, categoryId, amount, description);

        accountsService.applyOperation(operation, false);
        storage.getOperations().add(operation);

        return operation;
    }

    public boolean deleteOperation(Operation operation) {
        accountsService.applyOperation(operation, true);
        return storage.getOperations().remove(operation);
    }

}
