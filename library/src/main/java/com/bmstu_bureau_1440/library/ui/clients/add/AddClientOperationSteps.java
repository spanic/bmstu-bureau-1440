package com.bmstu_bureau_1440.library.ui.clients.add;

import com.bmstu_bureau_1440.library.ui.clients.add.AddClientOperationOrchestrator.AddClientSteps;
import com.bmstu_bureau_1440.library.ui.models.ParametrizedStepExecutor;
import com.bmstu_bureau_1440.shared.io.IO;

public class AddClientOperationSteps {

    public static class EnterClientNameStep implements ParametrizedStepExecutor<AddClientOperationContext> {

        @Override
        public AddClientSteps execute(AddClientOperationContext context) {
            context.getClient().setName(IO.inputString("Укажите имя читателя:"));
            return AddClientSteps.EMAIL;
        }

    }

    public static class EnterClientEmailStep implements ParametrizedStepExecutor<AddClientOperationContext> {

        @Override
        public AddClientSteps execute(AddClientOperationContext context) {
            context.getClient().setEmail(IO.inputString("Укажите email читателя (необязательно):"));
            return AddClientSteps.CONFIRMATION;
        }

    }

    public static class AddClientConfirmationStep implements ParametrizedStepExecutor<AddClientOperationContext> {

        @Override
        public AddClientSteps execute(AddClientOperationContext context) {
            var savedClient = context.getClientRepository().save(context.getClient());
            IO.displaySuccess("Читатель успешно добавлен: " + savedClient.getId());
            return null;
        }

    }

}
