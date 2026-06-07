package com.bmstu_bureau_1440.library.ui;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

public abstract class OperationOrchestrator<K> {

    protected final LinkedHashMap<Enum<?>, Supplier<StepExecutor<K>>> stepExecutors = new LinkedHashMap<>();

    protected final K context;

    protected Enum<?> currentStep;

    protected OperationOrchestrator(K context) {
        this.context = context;
    }

    public void execute() {
        if (currentStep == null) {
            if (stepExecutors.isEmpty()) {
                return;
            }
            currentStep = stepExecutors.firstEntry().getKey();
        }

        while (currentStep != null) {
            var nextStepExecutorSupplier = stepExecutors.get(currentStep);

            if (nextStepExecutorSupplier == null) {
                return;
            }

            currentStep = nextStepExecutorSupplier.get().execute(context);
        }
    }
}
