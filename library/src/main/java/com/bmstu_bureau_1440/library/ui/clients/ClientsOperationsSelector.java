package com.bmstu_bureau_1440.library.ui.clients;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.clients.add.AddClientOperationContext;
import com.bmstu_bureau_1440.library.ui.clients.add.AddClientOperationOrchestrator;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

import jakarta.annotation.PostConstruct;

@Component
public class ClientsOperationsSelector extends MenuSelector {

    @Autowired
    private ObjectProvider<AddClientOperationContext> contextProvider;

    @PostConstruct
    private void init() {
        executors.put(ClientsOperations.ADD_CLIENT, () -> {
            new AddClientOperationOrchestrator(contextProvider.getObject()).run();
        });
    }

    @Override
    protected String getLabel() {
        return "Выберите действие с читателями:";
    }

}
