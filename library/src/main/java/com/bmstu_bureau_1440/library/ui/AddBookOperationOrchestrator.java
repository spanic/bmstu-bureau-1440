package com.bmstu_bureau_1440.library.ui;

public class AddBookOperationOrchestrator extends OperationOrchestrator<AddBookOperationContext> {

    {
        stepExecutors.put(AddBookSteps.TITLE, EnterBookTitleStep::new);
        stepExecutors.put(AddBookSteps.AUTHOR, EnterBookAuthorStep::new);
    }

    public AddBookOperationOrchestrator() {
        super(new AddBookOperationContext());
    }

    enum AddBookSteps {
        TITLE,
        AUTHOR,
        ISBN,
        CONFIRMATION
    }

}
