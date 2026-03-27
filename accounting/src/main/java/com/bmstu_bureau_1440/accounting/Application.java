package com.bmstu_bureau_1440.accounting;

import com.bmstu_bureau_1440.accounting.io.Operation;
import com.bmstu_bureau_1440.accounting.repositories.FileStorageRepository;
import com.bmstu_bureau_1440.accounting.services.AnalyticsService;
import com.bmstu_bureau_1440.shared.io.IO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Application {

    private AnalyticsService service;

    private Storage storage;

    private FileStorageRepository repository;

    public void run() {

        while (true) {
            var option = IO.displayMenu(Operation.values());

            try {
                if (Operation.EXIT.getOperation().equals(option)) {
                    break;
                }

            } catch (Exception e) {
                IO.displayError(e);
            }

        }

    }

}
