package com.bmstu_bureau_1440.library.ui.clients;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.ui.clients.add.AddClientOperationContext;
import com.bmstu_bureau_1440.library.ui.clients.add.AddClientOperationOrchestrator;
import com.bmstu_bureau_1440.library.ui.clients.view.ViewClientsOperationExecutor;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

import jakarta.annotation.PostConstruct;

@Component
public class ClientsOperationsSelector extends MenuSelector {

    @Autowired
    private ObjectProvider<AddClientOperationContext> contextProvider;

    @Autowired
    private ObjectProvider<ViewClientsOperationExecutor> viewClientsOperationExecutorProvider;

    @PostConstruct
    private void init() {
        executors.put(ClientsOperations.ADD_CLIENT,
                new AddClientOperationOrchestrator(contextProvider.getObject()));
        executors.put(ClientsOperations.VIEW_CLIENTS,
                viewClientsOperationExecutorProvider.getObject());
    }

    @Override
    protected String getLabel() {
        return "Выберите действие с читателями:";
    }

}
