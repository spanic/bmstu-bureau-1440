package com.bmstu_bureau_1440.accounting;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.io.AccountingTUI;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.models.Category;
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
                                new BankAccount("Test account 1", BigDecimal.valueOf(994_341.92)),
                                new BankAccount("Test account 2", BigDecimal.valueOf(93_11.00)),
                                new BankAccount("Test account 3", BigDecimal.valueOf(4111.00)),
                                new BankAccount("Test account 4", BigDecimal.valueOf(1_532_112.00)),
                                new BankAccount("Test account 5", BigDecimal.valueOf(0.00)),
                                new BankAccount("Test account 6", BigDecimal.valueOf(0.00)),
                                new BankAccount("Test account 7", BigDecimal.valueOf(100.00))));
                storage.getCategories().addAll(List.of(
                                new Category(OperationType.DEPOSIT, "Salary"),
                                new Category(OperationType.WITHDRAWAL, "Games")));
                storage.getOperations().addAll(List.of(
                                new Operation(storage.getAccounts().get(0).getId(),
                                                storage.getCategories().get(0).getId(), BigDecimal.valueOf(20),
                                                "Lorem ipsum dolor sit amet consectetur adipisicing elit. Quisquam, quos."),
                                new Operation(storage.getAccounts().get(1).getId(),
                                                storage.getCategories().get(1).getId(), BigDecimal.ZERO,
                                                "Lorem ipsum dolor sit amet")));
                tui.run();
        }

}
