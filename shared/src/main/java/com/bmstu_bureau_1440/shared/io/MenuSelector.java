package com.bmstu_bureau_1440.shared.io;

import static com.bmstu_bureau_1440.shared.io.Operation.EXIT;

import java.util.LinkedHashMap;

public abstract class MenuSelector implements Runnable {

    protected final LinkedHashMap<ListOption, Runnable> executors = new LinkedHashMap<>();

    protected String getLabel() {
        return "Выберите действие:";
    }

    @Override
    public final void run() {
        do {
            try {
                var selectedOption = IO.inputListOptions(getLabel(),
                        Operation.withExit(executors.sequencedKeySet().toArray(ListOption[]::new)));

                if (EXIT.equals(selectedOption))
                    return;

                executors.entrySet().stream()
                        .filter(e -> e.getKey().equals(selectedOption))
                        .findFirst()
                        .orElseThrow()
                        .getValue()
                        .run();
            } catch (Exception e) {
                IO.displayError(e);
            }
        } while (loop());
    }

    protected boolean loop() {
        return false;
    }
}
