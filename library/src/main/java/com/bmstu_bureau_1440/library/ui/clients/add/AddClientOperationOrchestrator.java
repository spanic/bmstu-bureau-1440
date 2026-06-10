package com.bmstu_bureau_1440.library.ui.clients.add;

import com.bmstu_bureau_1440.library.ui.models.OperationOrchestrator;

public class AddClientOperationOrchestrator extends OperationOrchestrator<AddClientOperationContext> {

    public enum AddClientSteps {
        NAME,
        EMAIL,
        CONFIRMATION
    }

    public AddClientOperationOrchestrator(AddClientOperationContext context) {
        super(context);
        stepExecutors.put(AddClientSteps.NAME, AddClientOperationSteps.EnterClientNameStep::new);
        stepExecutors.put(AddClientSteps.EMAIL, AddClientOperationSteps.EnterClientEmailStep::new);
        stepExecutors.put(AddClientSteps.CONFIRMATION, AddClientOperationSteps.AddClientConfirmationStep::new);
    }

}
