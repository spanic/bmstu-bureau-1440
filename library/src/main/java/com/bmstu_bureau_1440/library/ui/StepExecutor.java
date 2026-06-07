package com.bmstu_bureau_1440.library.ui;

@FunctionalInterface
public interface StepExecutor<K> {

    Enum<?> execute(K context);

}
