package com.bmstu_bureau_1440.accounting;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.io.AccountingTUI;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.models.FileType;
import com.bmstu_bureau_1440.accounting.models.Operation;
import com.bmstu_bureau_1440.accounting.models.OperationType;
import com.bmstu_bureau_1440.accounting.repositories.FileStorageRepository;
import com.bmstu_bureau_1440.accounting.services.AnalyticsService;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Component
@AllArgsConstructor
public class Application {

        private AnalyticsService service;

        private FileStorageRepository repository;

        private Storage storage;

        private AccountingTUI tui;

        @SneakyThrows
        public void run() {
                storage.getAccounts().addAll(List.of(
                                new BankAccount("My account", BigDecimal.valueOf(994_341.92)),
                                new BankAccount("Another account", BigDecimal.valueOf(1000.00)),
                                new BankAccount("My savings", BigDecimal.valueOf(0.00))));
                storage.getCategories().addAll(List.of(
                                new Category(OperationType.DEPOSIT, "Salary"),
                                new Category(OperationType.DEPOSIT, "Gift"),
                                new Category(OperationType.WITHDRAWAL, "Food"),
                                new Category(OperationType.WITHDRAWAL, "Transport"),
                                new Category(OperationType.WITHDRAWAL, "Entertainment"),
                                new Category(OperationType.WITHDRAWAL, "Other")));
                storage.getOperations().addAll(List.of(
                                new Operation(storage.getAccounts().get(0).getId(),
                                                storage.getCategories().get(0).getId(), BigDecimal.valueOf(20),
                                                "Test operation"),
                                new Operation(storage.getAccounts().get(1).getId(),
                                                storage.getCategories().get(1).getId(), BigDecimal.ZERO,
                                                "Test operation")));

                repository.exportToFile(FileType.CSV);
                repository.exportToFile(FileType.JSON);
                repository.exportToFile(FileType.YAML);

                // tui.run();
        }

}
