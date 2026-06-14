package com.bmstu_bureau_1440.library.ui.models;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

public abstract class OperationOrchestrator<K> implements ParametrizedStepExecutor<K> {

    protected final LinkedHashMap<Enum<?>, Supplier<ParametrizedStepExecutor<K>>> stepExecutors = new LinkedHashMap<>();

    private final K context;

    private Enum<?> currentStep;

    protected OperationOrchestrator(K context) {
        this.context = context;
    }

    @Override
    public void run() {
        if (currentStep == null) {
            if (stepExecutors.isEmpty()) {
                throw new IllegalStateException("There're no executors found");
            }
            currentStep = stepExecutors.firstEntry().getKey();
        }

        while (currentStep != null) {
            var nextStepExecutorSupplier = stepExecutors.get(currentStep);

            if (nextStepExecutorSupplier == null) {
                throw new IllegalStateException("There're no executors for the current step");
            }

            currentStep = nextStepExecutorSupplier.get().execute(context);
        }

    }

    @Override
    public Enum<?> execute(K context) {
        throw new UnsupportedOperationException("This method is not supported for the current operations orchestrator");
    }

}
