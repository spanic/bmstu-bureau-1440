package com.bmstu_bureau_1440.accounting.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.models.Operation;
import com.bmstu_bureau_1440.accounting.models.OperationType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final Storage storage;

    private final CategoriesService categoriesService;

    public record AnalyticsResult(BigDecimal totalBalance, BigDecimal totalIncome, BigDecimal totalExpenses) {
    }

    public AnalyticsResult getAnalytics() {

        BigDecimal totalBalance = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        totalBalance = storage.getAccounts().stream().map(BankAccount::getBalance).reduce(BigDecimal.ZERO,
                BigDecimal::add);

        for (Operation operation : storage.getOperations()) {
            final Category category = categoriesService.getCategoryById(operation.getCategoryId());

            switch (category.getType()) {
                case OperationType.DEPOSIT:
                    totalIncome = totalIncome.add(operation.getAmount());
                    break;
                case OperationType.WITHDRAWAL:
                    totalExpenses = totalExpenses.add(operation.getAmount());
                    break;
                default:
                    break;
            }
        }

        return new AnalyticsResult(totalBalance, totalIncome, totalExpenses);
    }

}
