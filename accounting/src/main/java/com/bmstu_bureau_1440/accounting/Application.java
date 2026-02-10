package com.bmstu_bureau_1440.accounting;

import com.bmstu_bureau_1440.accounting.models.FileType;
import com.bmstu_bureau_1440.accounting.models.Transaction;
import com.bmstu_bureau_1440.accounting.repositories.FileStorageRepository;
import com.bmstu_bureau_1440.accounting.services.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Application {

    private AnalyticsService service;

    private Storage storage;

    private FileStorageRepository repository;

    public void run() {
        storage.getTransactions().add(new Transaction());
        repository.exportToFile(FileType.CSV);
        repository.importFromFile(FileType.CSV);
    }

}
