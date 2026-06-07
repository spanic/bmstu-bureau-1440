package com.bmstu_bureau_1440.library;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.AddBookOperationOrchestrator;
import com.bmstu_bureau_1440.library.ui.Operation;
import com.bmstu_bureau_1440.library.ui.OperationOrchestrator;
import com.bmstu_bureau_1440.shared.io.IO;

@Component
@Profile("runtime")
public class LibraryConsoleUI implements CommandLineRunner {

    final LinkedHashMap<String, Supplier<OperationOrchestrator<?>>> stepExecutors = new LinkedHashMap<>();
    {
        stepExecutors.put(Operation.ADD_BOOK.getOperation(), AddBookOperationOrchestrator::new);
    }

    @Override
    public void run(String... args) {
        var operation = IO.displayMenuWithExit(Operation.values());
        var orchestrator = stepExecutors.get(operation).get();
        orchestrator.execute();
    }
}
