package com.bmstu_bureau_1440.library.ui;

import com.bmstu_bureau_1440.library.ui.AddBookOperationOrchestrator.AddBookSteps;
import com.bmstu_bureau_1440.shared.io.IO;

public class EnterBookAuthorStep implements StepExecutor<AddBookOperationContext> {

    @Override
    public AddBookSteps execute(AddBookOperationContext context) {
        context.getBook().setAuthor(IO.inputString("Enter book author:"));
        return null;
    }

}
