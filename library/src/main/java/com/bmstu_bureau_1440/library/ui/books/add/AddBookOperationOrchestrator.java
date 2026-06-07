package com.bmstu_bureau_1440.library.ui.books.add;

import com.bmstu_bureau_1440.library.ui.books.add.steps.EnterBookAuthorStep;
import com.bmstu_bureau_1440.library.ui.books.add.steps.EnterBookTitleStep;
import com.bmstu_bureau_1440.library.ui.models.OperationOrchestrator;

public class AddBookOperationOrchestrator extends OperationOrchestrator<AddBookOperationContext> {

    public enum AddBookSteps {
        TITLE,
        AUTHOR,
        ISBN,
        CONFIRMATION
    }

    {
        stepExecutors.put(AddBookSteps.TITLE, EnterBookTitleStep::new);
        stepExecutors.put(AddBookSteps.AUTHOR, EnterBookAuthorStep::new);
    }

    public AddBookOperationOrchestrator() {
        super(new AddBookOperationContext());
    }

}
