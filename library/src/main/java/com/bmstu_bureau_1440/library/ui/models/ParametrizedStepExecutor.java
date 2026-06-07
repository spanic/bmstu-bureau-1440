package com.bmstu_bureau_1440.library.ui.models;

public interface ParametrizedStepExecutor<K> extends Runnable {

    @Override
    default void run() {
        throw new UnsupportedOperationException("This method is not supported for parametrized step executor");
    }

    Enum<?> execute(K context);

}
