package com.bmstu_bureau_1440.library.ui.operations;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.operations.add.AddOperationContext;
import com.bmstu_bureau_1440.library.ui.operations.add.AddOperationOrchestrator;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

import jakarta.annotation.PostConstruct;

@Component
public class OperationsSelector extends MenuSelector {

    @Autowired
    private ObjectProvider<AddOperationContext> addOperationContextProvider;

    @PostConstruct
    private void init() {
        executors.put(Operations.PROVIDE_BOOK,
                () -> new AddOperationOrchestrator(addOperationContextProvider.getObject()).run());
    }

}
