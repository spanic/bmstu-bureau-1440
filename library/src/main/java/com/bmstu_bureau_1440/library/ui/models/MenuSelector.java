package com.bmstu_bureau_1440.library.ui.models;

import static com.bmstu_bureau_1440.shared.io.Operation.EXIT;

import java.util.LinkedHashMap;

import com.bmstu_bureau_1440.shared.io.IO;
import com.bmstu_bureau_1440.shared.io.IOperation;

public abstract class MenuSelector implements Runnable {

    protected final LinkedHashMap<IOperation, Runnable> executors = new LinkedHashMap<>();

    @Override
    public final void run() {
        do {
            String selected = IO.displayMenuWithExit(
                    executors.sequencedKeySet().toArray(IOperation[]::new));

            if (EXIT.getOperation().equals(selected))
                return;

            executors.entrySet().stream()
                    .filter(e -> e.getKey().getOperation().equals(selected))
                    .findFirst()
                    .orElseThrow()
                    .getValue()
                    .run();
        } while (loop());
    }

    protected boolean loop() {
        return false;
    }

}
