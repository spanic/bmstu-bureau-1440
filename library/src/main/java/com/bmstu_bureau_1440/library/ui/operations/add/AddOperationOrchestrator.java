package com.bmstu_bureau_1440.library.ui.operations.add;

import com.bmstu_bureau_1440.library.ui.models.OperationOrchestrator;

public class AddOperationOrchestrator extends OperationOrchestrator<AddOperationContext> {

    public enum Steps {
        CLIENT,
        BOOK,
        CONFIRMATION
    }

    public AddOperationOrchestrator(AddOperationContext context) {
        super(context);
        stepExecutors.put(Steps.CLIENT, AddOperationSteps.ChooseClientStep::new);
        stepExecutors.put(Steps.BOOK, AddOperationSteps.ChooseBookStep::new);
        stepExecutors.put(Steps.CONFIRMATION, AddOperationSteps.ConfirmationStep::new);
    }

}
