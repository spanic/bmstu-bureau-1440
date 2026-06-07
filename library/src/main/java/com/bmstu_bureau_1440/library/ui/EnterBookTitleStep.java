package com.bmstu_bureau_1440.library.ui;

import com.bmstu_bureau_1440.library.ui.AddBookOperationOrchestrator.AddBookSteps;
import com.bmstu_bureau_1440.shared.io.IO;

public class EnterBookTitleStep implements StepExecutor<AddBookOperationContext> {

    @Override
    public AddBookSteps execute(AddBookOperationContext context) {
        context.getBook().setTitle(IO.inputString("Enter book title:"));
        return AddBookSteps.AUTHOR;
    }

}
