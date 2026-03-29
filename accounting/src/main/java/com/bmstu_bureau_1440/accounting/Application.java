package com.bmstu_bureau_1440.accounting;

import com.bmstu_bureau_1440.accounting.io.AccountingTUI;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.repositories.FileStorageRepository;
import com.bmstu_bureau_1440.accounting.services.AnalyticsService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

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
                new BankAccount("Test account 7", BigDecimal.valueOf(100.00)))
        );
        tui.run();
    }

}
