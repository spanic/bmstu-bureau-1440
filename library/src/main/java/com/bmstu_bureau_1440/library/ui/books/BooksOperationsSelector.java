package com.bmstu_bureau_1440.library.ui.books;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.books.add.AddBookOperationContext;
import com.bmstu_bureau_1440.library.ui.books.add.AddBookOperationOrchestrator;
import com.bmstu_bureau_1440.library.ui.books.find.FindBookOperationContext;
import com.bmstu_bureau_1440.library.ui.books.find.FindBookOperationOrchestrator;
import com.bmstu_bureau_1440.library.ui.books.view.ViewBooksOperationExecutor;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

import jakarta.annotation.PostConstruct;

@Component
public class BooksOperationsSelector extends MenuSelector {

    @Autowired
    private ObjectProvider<AddBookOperationContext> addBookOperationContextProvider;

    @Autowired
    private ObjectProvider<ViewBooksOperationExecutor> viewBooksOperationExecutorProvider;

    @Autowired
    private ObjectProvider<FindBookOperationContext> findBookOperationContextProvider;

    @PostConstruct
    private void init() {
        executors.put(BooksOperations.ADD_BOOK,
                () -> new AddBookOperationOrchestrator(addBookOperationContextProvider.getObject()).run());
        executors.put(BooksOperations.VIEW_BOOKS,
                viewBooksOperationExecutorProvider.getObject());
        executors.put(BooksOperations.FIND_BOOK_BY_TITLE,
                () -> new FindBookOperationOrchestrator(findBookOperationContextProvider.getObject()).run());
    }

    @Override
    protected String getLabel() {
        return "Выберите действие с книгами:";
    }

}
