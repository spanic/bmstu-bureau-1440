package com.bmstu_bureau_1440.library.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.books.BooksOperationsSelector;
import com.bmstu_bureau_1440.library.ui.models.MenuSelector;

@Component
@Profile("runtime")
public class LibraryConsoleUI extends MenuSelector implements CommandLineRunner {

    {
        executors.put(MainOperations.LIST_BOOKS_OPERATIONS, BooksOperationsSelector::new);
    }

    @Override
    protected boolean loop() {
        return true;
    }

    @Override
    public void run(String... args) {
        run();
    }

}
