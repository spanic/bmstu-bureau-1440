package com.bmstu_bureau_1440.library.ui.books;

import com.bmstu_bureau_1440.library.ui.books.add.AddBookOperationOrchestrator;
import com.bmstu_bureau_1440.library.ui.models.MenuSelector;

public class BooksOperationsSelector extends MenuSelector {

    {
        executors.put(BooksOperations.ADD_BOOK, AddBookOperationOrchestrator::new);
    }

}
