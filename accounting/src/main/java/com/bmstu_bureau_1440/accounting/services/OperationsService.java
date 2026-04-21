package com.bmstu_bureau_1440.accounting.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.Operation;
import com.bmstu_bureau_1440.accounting.models.OperationType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationsService {

    private final Storage storage;

    private final AccountsService accountsService;

    private final CategoriesService categoriesService;

    public Operation addNewOperation(String accountId, String categoryId, BigDecimal amount, String description) {

        OperationType categoryType = categoriesService.getCategoryById(categoryId).getType();
        if (categoryType == OperationType.UNKNOWN) {
            throw new IllegalArgumentException("Category type is unknown");
        }
        if (categoryType == OperationType.WITHDRAWAL) {
            amount = amount.negate();
        }

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
