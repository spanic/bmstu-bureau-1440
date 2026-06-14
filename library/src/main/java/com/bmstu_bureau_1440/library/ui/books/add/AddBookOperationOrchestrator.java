package com.bmstu_bureau_1440.library.ui.books.add;

import com.bmstu_bureau_1440.library.ui.models.OperationOrchestrator;

public class AddBookOperationOrchestrator extends OperationOrchestrator<AddBookOperationContext> {

    public enum AddBookSteps {
        TITLE,
        AUTHOR,
        CONFIRMATION,
        GENRE
    }

    public AddBookOperationOrchestrator(AddBookOperationContext context) {
        super(context);
        stepExecutors.put(AddBookSteps.TITLE, AddBookOperationSteps.EnterBookTitleStep::new);
        stepExecutors.put(AddBookSteps.AUTHOR, AddBookOperationSteps.EnterBookAuthorStep::new);
        stepExecutors.put(AddBookSteps.CONFIRMATION, AddBookOperationSteps.AddBookConfirmationStep::new);
        stepExecutors.put(AddBookSteps.GENRE, AddBookOperationSteps.SelectBookGenreStep::new);
    }

}
