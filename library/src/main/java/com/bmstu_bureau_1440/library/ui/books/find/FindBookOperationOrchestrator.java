package com.bmstu_bureau_1440.library.ui.books.find;

import com.bmstu_bureau_1440.library.ui.models.OperationOrchestrator;

public class FindBookOperationOrchestrator extends OperationOrchestrator<FindBookOperationContext> {

    public enum FindBookSteps {
        ENTER_TITLE,
        VIEW_RESULTS
    }

    public FindBookOperationOrchestrator(FindBookOperationContext context) {
        super(context);
        stepExecutors.put(FindBookSteps.ENTER_TITLE, FindBookOperationSteps.EnterBookTitleStep::new);
        stepExecutors.put(FindBookSteps.VIEW_RESULTS, FindBookOperationSteps.ViewResultsStep::new);
    }

}
