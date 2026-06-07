package com.bmstu_bureau_1440.library.ui.books.add.steps;

import com.bmstu_bureau_1440.library.ui.books.add.AddBookOperationContext;
import com.bmstu_bureau_1440.library.ui.books.add.AddBookOperationOrchestrator.AddBookSteps;
import com.bmstu_bureau_1440.library.ui.models.ParametrizedStepExecutor;
import com.bmstu_bureau_1440.shared.io.IO;

public class EnterBookAuthorStep implements ParametrizedStepExecutor<AddBookOperationContext> {

    @Override
    public AddBookSteps execute(AddBookOperationContext context) {
        context.getBook().setAuthor(IO.inputString("Enter book author:"));
        return null;
    }

}
