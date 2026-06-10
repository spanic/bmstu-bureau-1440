package com.bmstu_bureau_1440.library.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.books.BooksOperationsSelector;
import com.bmstu_bureau_1440.library.ui.clients.ClientsOperationsSelector;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

import jakarta.annotation.PostConstruct;

@Component
@Profile("runtime")
public class LibraryConsoleUI extends MenuSelector implements CommandLineRunner {

    @Autowired
    private BooksOperationsSelector booksSelector;

    @Autowired
    private ClientsOperationsSelector clientsSelector;

    @PostConstruct
    private void init() {
        executors.put(MainOperations.LIST_BOOKS_OPERATIONS, booksSelector);
        executors.put(MainOperations.LIST_CLIENTS_OPERATIONS, clientsSelector);
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
