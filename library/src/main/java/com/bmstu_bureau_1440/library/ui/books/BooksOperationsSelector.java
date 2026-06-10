package com.bmstu_bureau_1440.library.ui.books;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.books.add.AddBookOperationContext;
import com.bmstu_bureau_1440.library.ui.books.add.AddBookOperationOrchestrator;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

import jakarta.annotation.PostConstruct;

@Component
public class BooksOperationsSelector extends MenuSelector {

    @Autowired
    private ObjectProvider<AddBookOperationContext> contextProvider;

    @PostConstruct
    private void init() {
        executors.put(BooksOperations.ADD_BOOK, () -> {
            new AddBookOperationOrchestrator(contextProvider.getObject()).run();
        });
    }

    @Override
    protected String getLabel() {
        return "Выберите действие с книгами:";
    }

}
