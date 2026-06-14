package com.bmstu_bureau_1440.library.ui.operations;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.operations.add.AddOperationContext;
import com.bmstu_bureau_1440.library.ui.operations.add.AddOperationOrchestrator;
import com.bmstu_bureau_1440.library.ui.operations.view.ViewWithdrawnBooksOperationExecutor;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

import jakarta.annotation.PostConstruct;

@Component
public class OperationsSelector extends MenuSelector {

    @Autowired
    private ObjectProvider<AddOperationContext> addOperationContextProvider;

    @Autowired
    private ObjectProvider<ViewWithdrawnBooksOperationExecutor> viewWithdrawnBooksOperationExecutorProvider;

    @PostConstruct
    private void init() {
        executors.put(Operations.ADD_OPERATION,
                () -> new AddOperationOrchestrator(addOperationContextProvider.getObject()).run());
        executors.put(Operations.VIEW_WITHDRAWN_BOOKS, viewWithdrawnBooksOperationExecutorProvider.getObject());
    }

}
